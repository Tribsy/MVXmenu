# Spacing Scale

## Scale Definition

All spacing derived from 4px base unit (SP_1). Uses 4px, 8px, 12px, 16px, 24px, 32px, 48px, 64px.

## Token Reference

| Token | Pixels | Relative | CSS Equivalent |
|-------|--------|----------|----------------|
| SP_1 | 4px | 0.25rem | --spacing-1 |
| SP_2 | 8px | 0.5rem | --spacing-2 |
| SP_3 | 12px | 0.75rem | --spacing-3 |
| SP_4 | 16px | 1rem | --spacing-4 |
| SP_6 | 24px | 1.5rem | --spacing-6 |
| SP_8 | 32px | 2rem | --spacing-8 |
| SP_12 | 48px | 3rem | --spacing-12 |
| SP_16 | 64px | 4rem | --spacing-16 |

## Usage Patterns

### Component Internal
- Button padding: SP_2 (8px) horizontal, SP_1 (4px) vertical
- Input padding: SP_2 (8px) horizontal, SP_1 (4px) vertical
- Card padding: SP_3 (12px) all sides
- Dropdown item padding: SP_2 (8px) horizontal, SP_1 (4px) vertical

### Layout
- Panel padding: SP_4 (16px)
- Section gap: SP_6 (24px)
- Sidebar item gap: SP_1 (4px)
- Module card gap: SP_2 (8px)

### Responsive Adjustments
At GUI scale ≥3:
- Panel padding: SP_3 (12px)
- Section gap: SP_4 (16px)
- Card gap: SP_1 (4px)