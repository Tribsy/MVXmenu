#!/usr/bin/env python3
"""
Generate MVXmenu font resource pack.
Run: python scripts/generate_font_pack.py

Creates assets/mvxmenu/font/ with TTF fonts and default.json provider.
"""

import os
import shutil
import json

SOURCE_DIR = 'docs/brand/typography/resource-pack/'
OUTPUT_DIR = 'src/main/resources/assets/mvxmenu/font/'

FONT_FILES = [
    'JetBrainsMono-Regular.ttf',
    'JetBrainsMono-Medium.ttf',
    'JetBrainsMono-Bold.ttf',
    'Inter-Regular.ttf',
    'Inter-Medium.ttf',
    'Inter-Bold.ttf',
    'PressStart2P-Regular.ttf',
]

def generate_font_pack():
    # Create output directory
    os.makedirs(OUTPUT_DIR, exist_ok=True)
    
    # Copy font files
    for font_file in FONT_FILES:
        src = os.path.join(SOURCE_DIR, font_file)
        dst = os.path.join(OUTPUT_DIR, font_file)
        if os.path.exists(src):
            shutil.copy2(src, dst)
            print(f'  Copied {font_file}')
        else:
            print(f'  WARNING: {src} not found')
    
    # Create pack.mcmeta
    pack_mcmeta = {
        "pack": {
            "pack_format": 15,
            "description": "MVXmenu Custom Fonts"
        }
    }
    with open(os.path.join(OUTPUT_DIR, 'pack.mcmeta'), 'w') as f:
        json.dump(pack_mcmeta, f, indent=2)
    print('  Created pack.mcmeta')
    
    # Create default.json font provider
    font_provider = {
        "providers": [
            {
                "type": "ttf",
                "file": "mvxmenu:font/JetBrainsMono-Regular.ttf",
                "shift": 0,
                "size": 12,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/JetBrainsMono-Medium.ttf",
                "shift": 0,
                "size": 12,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/JetBrainsMono-Bold.ttf",
                "shift": 0,
                "size": 12,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/Inter-Regular.ttf",
                "shift": 0,
                "size": 11,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/Inter-Medium.ttf",
                "shift": 0,
                "size": 11,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/Inter-Bold.ttf",
                "shift": 0,
                "size": 11,
                "oversample": 2
            },
            {
                "type": "ttf",
                "file": "mvxmenu:font/PressStart2P-Regular.ttf",
                "shift": 0,
                "size": 12,
                "oversample": 2
            }
        ]
    }
    
    with open(os.path.join(OUTPUT_DIR, 'default.json'), 'w') as f:
        json.dump(font_provider, f, indent=2)
    print('  Created default.json')
    
    print(f'Font pack generated at {OUTPUT_DIR}')

if __name__ == '__main__':
    generate_font_pack()