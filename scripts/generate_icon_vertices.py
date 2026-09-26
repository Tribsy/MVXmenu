#!/usr/bin/env python3
"""
Generate IconVertexData.java from SVG files in docs/brand/icons/svg/
Run: python scripts/generate_icon_vertices.py

Parses SVG path data, normalizes to 0-1 coordinates, outputs float[][] arrays.
"""

import re
import os
import xml.etree.ElementTree as ET
from math import cos, sin, pi

SVG_DIR = 'docs/brand/icons/svg/'
OUTPUT_PATH = 'src/main/java/dev/mvxmenu/theme/IconVertexData.java'

# SVG namespace
NS = {'svg': 'http://www.w3.org/2000/svg'}

def parse_path_data(d):
    """Parse SVG path 'd' attribute into list of (x, y) points."""
    # Handle M, L, H, V, C, Z commands
    tokens = re.findall(r'([MLHVCZ])\s*([^MLHVCZ]*)', d, re.IGNORECASE)
    points = []
    current = (0, 0)
    start = (0, 0)
    
    for cmd, args in tokens:
        cmd = cmd.upper()
        nums = list(map(float, re.findall(r'-?\d+\.?\d*', args)))
        
        if cmd == 'M':  # Move to
            for i in range(0, len(nums), 2):
                if i + 1 < len(nums):
                    current = (nums[i], nums[i+1])
                    if not points:
                        start = current
                    points.append(current)
        
        elif cmd == 'L':  # Line to
            for i in range(0, len(nums), 2):
                if i + 1 < len(nums):
                    current = (nums[i], nums[i+1])
                    points.append(current)
        
        elif cmd == 'H':  # Horizontal line to
            for x in nums:
                current = (x, current[1])
                points.append(current)
        
        elif cmd == 'V':  # Vertical line to
            for y in nums:
                current = (current[0], y)
                points.append(current)
        
        elif cmd == 'C':  # Cubic Bezier
            for i in range(0, len(nums), 6):
                if i + 5 < len(nums):
                    x1, y1 = nums[i], nums[i+1]
                    x2, y2 = nums[i+2], nums[i+3]
                    x3, y3 = nums[i+4], nums[i+5]
                    # Flatten to 8 segments
                    for t in range(1, 9):
                        tt = t / 8.0
                        u = 1 - tt
                        uu = u * u
                        uuu = uu * u
                        tt2 = tt * tt
                        ttt = tt2 * tt
                        
                        x = uuu * current[0] + 3 * uu * tt * x1 + 3 * u * tt2 * x2 + ttt * x3
                        y = uuu * current[1] + 3 * uu * tt * y1 + 3 * u * tt2 * y2 + ttt * y3
                        current = (x, y)
                        points.append(current)
        
        elif cmd == 'Z':  # Close path
            if points and points[0] != points[-1]:
                points.append(start)
    
    return points

def normalize_points(points):
    """Normalize points to 0-1 range (divide by 16)."""
    return [[x/16.0, y/16.0] for x, y in points]

def process_svg(filepath):
    """Extract path data from SVG file."""
    tree = ET.parse(filepath)
    root = tree.getroot()
    
    # Find path element
    path_elem = root.find('.//svg:path', NS)
    if path_elem is None:
        path_elem = root.find('.//{http://www.w3.org/2000/svg}path')
    
    if path_elem is None:
        return None
    
    d = path_elem.get('d', '')
    points = parse_path_data(d)
    return normalize_points(points)

def generate_icon_data():
    icons = {}
    
    for filename in sorted(os.listdir(SVG_DIR)):
        if filename.endswith('.svg'):
            name = filename[:-4].upper()
            filepath = os.path.join(SVG_DIR, filename)
            points = process_svg(filepath)
            if points:
                icons[name] = points
                print(f'  {name}: {len(points)} vertices')
            else:
                print(f'  {name}: NO PATH FOUND')
    
    # Generate Java
    lines = []
    lines.append('package dev.mvxmenu.theme;')
    lines.append('')
    lines.append('/**')
    lines.append(' * Icon vertex data generated from docs/brand/icons/svg/*.svg')
    lines.append(' * DO NOT EDIT MANUALLY -- run scripts/generate_icon_vertices.py')
    lines.append(' *')
    lines.append(' * Each icon: float[][] where each float[] = {x, y} normalized to 0-1 (16x16 viewport)')
    lines.append(' * Render with IconVertexData.renderIcon()')
    lines.append(' */')
    lines.append('public final class IconVertexData {')
    lines.append('')
    
    for name, points in sorted(icons.items()):
        lines.append(f'    public static final float[][] {name} = {{')
        for i, (x, y) in enumerate(points):
            lines.append(f'        {{ {x:.6f}f, {y:.6f}f }}{"," if i < len(points)-1 else ""}')
        lines.append('    };')
        lines.append('')
    
    # Render method (stub - VertexConsumer API not available in 1.21.4 DrawContext)
    lines.append('    /**')
    lines.append('     * Renders an icon using VertexConsumer line strip (stub for future implementation).')
    lines.append('     * @param context DrawContext')
    lines.append('     * @param path float[][] from icon constants (e.g. SHIELD)')
    lines.append('     * @param x Screen X')
    lines.append('     * @param y Screen Y')
    lines.append('     * @param size Icon size in pixels')
    lines.append('     * @param color ARGB color')
    lines.append('     * @param strokeWidth Stroke width in pixels (at 16x16 viewport)')
    lines.append('     */')
    lines.append('    public static void renderIcon(net.minecraft.client.gui.DrawContext context, float[][] path, int x, int y,')
    lines.append('                                     int size, int color, float strokeWidth) {')
    lines.append('        // TODO: Implement when VertexConsumer API is available in DrawContext')
    lines.append('        // For now, use MvxmenuIcons enum render() method')
    lines.append('    }')
    lines.append('')
    lines.append('    private IconVertexData() {}')
    lines.append('}')
    
    output = '\n'.join(lines)
    
    os.makedirs(os.path.dirname(OUTPUT_PATH), exist_ok=True)
    with open(OUTPUT_PATH, 'w', encoding='utf-8') as f:
        f.write(output)
    
    print(f'Generated {OUTPUT_PATH} with {len(icons)} icons')

if __name__ == '__main__':
    generate_icon_data()