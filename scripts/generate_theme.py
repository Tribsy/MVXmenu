#!/usr/bin/env python3
"""
Generate MvxmenuTheme.java from docs/brand/colors/palette.md
Run: python scripts/generate_theme.py
"""

import re
import os

# Parse palette.md for color definitions
def parse_palette(filepath):
    colors = {}
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Match table rows with token, hex, argb
    pattern = r'\|\s*(`?\w+`?)?\s*\|\s*(#[0-9A-Fa-f]{6})?\s*\|\s*(0x[0-9A-Fa-f]{8})?\s*\|'
    matches = re.findall(pattern, content)
    
    for match in matches:
        token = match[0].strip('`') if match[0] else None
        hex_val = match[1] if match[1] else None
        argb = match[2] if match[2] else None
        
        if token and argb:
            colors[token] = argb
        elif token and hex_val:
            # Convert hex to ARGB (assume FF alpha)
            colors[token] = '0xFF' + hex_val[1:].upper()
    
    return colors

# Hardcoded values from palette.md (since parsing markdown tables is fragile)
THEME_DATA = {
    # Backgrounds
    'BG_0': '0xFF060606',
    'BG_1': '0xFF0E0E0E',
    'BG_2': '0xFF161616',
    'BG_3': '0xFF1E1E1E',
    'BG_4': '0xFF272727',
    'BG_5': '0xFF303030',
    # Borders
    'BD_0': '0xFF181818',
    'BD_1': '0xFF242424',
    'BD_2': '0xFF333333',
    'BD_3': '0xFF444444',
    # Text
    'TX_0': '0xFFEFEFEF',
    'TX_1': '0xFFA0A0A0',
    'TX_2': '0xFF5C5C5C',
    'TX_3': '0xFF333333',
    # Accent / Semantic
    'AC': '0xFF8B5CF6',
    'SUCCESS': '0xFF4ADE80',
    'WARNING': '0xFFFCD34D',
    'DANGER': '0xFFF87171',
    'INFO': '0xFF60A5FA',
    'PURPLE': '0xFFA78BFA',
    'ORANGE': '0xFFFB923C',
    # Semantic backgrounds (10%)
    'SUCCESS_BG': '0x1A4ADE80',
    'WARNING_BG': '0x1AFCD34D',
    'DANGER_BG': '0x1AF87171',
    'INFO_BG': '0x1A60A5FA',
    # Overlays
    'AC_DIM': '0x128B5CF6',
    'AC_BORDER': '0x388B5CF6',
    'AC_FG': '0xFF1A0D2E',
    'AC_GLOW': '0x2E8B5CF6',
    'PURPLE_BG': '0x1AA78BFA',
    # Radius
    'R_WINDOW': '19',
    'R_PANEL': '12',
    'R_CARD': '12',
    'R_BUTTON': '8',
    'R_INPUT': '6',
    'R_BADGE': '999',
    # Spacing
    'SP_1': '4',
    'SP_2': '8',
    'SP_3': '12',
    'SP_4': '16',
    'SP_6': '24',
    'SP_8': '32',
    'SP_12': '48',
    'SP_16': '64',
    # Typography
    'TYPE_HERO': '28',
    'TYPE_DISPLAY': '20',
    'TYPE_HEADING': '16',
    'TYPE_SUBHEADING': '13',
    'TYPE_DEFAULT': '12',
    'TYPE_BODY': '11',
    'TYPE_LABEL': '10',
    'TYPE_MICRO': '9',
    # Motion
    'MOTION_MICRO': '80',
    'MOTION_SNAP': '120',
    'MOTION_SLIDE': '200',
    'MOTION_FADE': '200',
    'MOTION_SPRING': '300',
    # Shadows
    'SHADOW_SM': '0x40000000',
    'SHADOW_MD': '0x60000000',
    'SHADOW_LG': '0x80000000',
    'SHADOW_PANEL': '0x08FFFFFF',
    # Fonts
    'FONT_UI': '"mvxmenu:jetbrains_mono"',
    'FONT_BODY': '"mvxmenu:inter"',
    'FONT_PIXEL': '"mvxmenu:press_start_2p"',
    # High Contrast
    'HC_BORDER': '0xFFFFFFFF',
    'HC_TEXT': '0xFFFFFFFF',
    'HC_BG': '0xFF000000',
}

def generate_theme():
    lines = []
    lines.append('package dev.mvxmenu.theme;')
    lines.append('')
    lines.append('/**')
    lines.append(' * Design tokens generated from docs/brand/colors/palette.md')
    lines.append(' * DO NOT EDIT MANUALLY -- run scripts/generate_theme.py')
    lines.append(' */')
    lines.append('public final class MvxmenuTheme {')
    lines.append('')
    
    # Group comments
    groups = [
        ('Backgrounds', ['BG_0', 'BG_1', 'BG_2', 'BG_3', 'BG_4', 'BG_5']),
        ('Borders', ['BD_0', 'BD_1', 'BD_2', 'BD_3']),
        ('Text', ['TX_0', 'TX_1', 'TX_2', 'TX_3']),
        ('Accent / Semantic', ['AC', 'SUCCESS', 'WARNING', 'DANGER', 'INFO', 'PURPLE', 'ORANGE']),
        ('Semantic Backgrounds (10% Opacity)', ['SUCCESS_BG', 'WARNING_BG', 'DANGER_BG', 'INFO_BG']),
        ('Overlay / Accent Variants', ['AC_DIM', 'AC_BORDER', 'AC_FG', 'AC_GLOW', 'PURPLE_BG']),
        ('Radius (pixels)', ['R_WINDOW', 'R_PANEL', 'R_CARD', 'R_BUTTON', 'R_INPUT', 'R_BADGE']),
        ('Spacing (pixels)', ['SP_1', 'SP_2', 'SP_3', 'SP_4', 'SP_6', 'SP_8', 'SP_12', 'SP_16']),
        ('Typography', ['TYPE_HERO', 'TYPE_DISPLAY', 'TYPE_HEADING', 'TYPE_SUBHEADING', 'TYPE_DEFAULT', 'TYPE_BODY', 'TYPE_LABEL', 'TYPE_MICRO']),
        ('Font Resource IDs', ['FONT_UI', 'FONT_BODY', 'FONT_PIXEL']),
        ('Motion Durations (ms)', ['MOTION_MICRO', 'MOTION_SNAP', 'MOTION_SLIDE', 'MOTION_FADE', 'MOTION_SPRING']),
        ('Elevation Shadows', ['SHADOW_SM', 'SHADOW_MD', 'SHADOW_LG', 'SHADOW_PANEL']),
        ('High Contrast Mode', ['HC_BORDER', 'HC_TEXT', 'HC_BG']),
    ]
    
    for group_name, tokens in groups:
        lines.append(f'    // {group_name}')
        for token in tokens:
            if token in THEME_DATA:
                val = THEME_DATA[token]
                if token.startswith('FONT_'):
                    lines.append(f'    public static final String {token} = {val};')
                else:
                    lines.append(f'    public static final int {token} = {val};')
        lines.append('')
    
    lines.append('    private MvxmenuTheme() {}')
    lines.append('}')
    
    output = '\n'.join(lines)
    
    # Write to src/main/java/dev/mvxmenu/theme/MvxmenuTheme.java
    output_path = 'src/main/java/dev/mvxmenu/theme/MvxmenuTheme.java'
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(output)
    
    print(f'Generated {output_path}')

if __name__ == '__main__':
    generate_theme()