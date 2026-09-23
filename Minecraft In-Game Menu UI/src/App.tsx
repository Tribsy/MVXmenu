import React, { useState } from 'react'
import {
  Check, ChevronRight, Settings, RotateCcw, X,
} from 'lucide-react'

// ─────────────────────────────────────────────
// CUSTOM SVG ICON SYSTEM
// All icons: 16×16 viewport, 1.5px stroke, round caps, currentColor.
// Designed on a pixel grid for HUD legibility at 12–20px render sizes.
// ─────────────────────────────────────────────
type IconProps = { size?: number; className?: string }

// Usage rule: these icons replace lucide for all in-client UI.
// Use lucide only for the DS viewer chrome (external tooling metaphor).

export const Icon = {
  /** Shield — protection / anti-cheat category */
  Shield: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M8 2L3 4v4c0 3 2.3 5 5 6 2.7-1 5-3 5-6V4L8 2Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round"/>
      <path d="M5.5 8l1.8 1.8L10.5 6" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  ),
  /** Crosshair — targeting / aim-assist modules */
  Crosshair: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <circle cx="8" cy="8" r="3" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M8 1v3M8 12v3M1 8h3M12 8h3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
      <circle cx="8" cy="8" r="1" fill="currentColor"/>
    </svg>
  ),
  /** Eye — visibility / render / ESP modules */
  Eye: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M1.5 8C3 4.5 5.3 3 8 3s5 1.5 6.5 5C13 12.5 10.7 13 8 13S3 12.5 1.5 8Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round"/>
      <circle cx="8" cy="8" r="2" stroke="currentColor" strokeWidth="1.5"/>
    </svg>
  ),
  /** Zap — speed / performance / sprint modules */
  Zap: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M9 1.5L4 9h4.5L7 14.5 12 7H7.5L9 1.5Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round" strokeLinecap="round"/>
    </svg>
  ),
  /** Key — keybind configuration */
  Key: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <circle cx="5.5" cy="6.5" r="3" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M8 8.5l5.5 5.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
      <path d="M11 11.5l1.5 1.5M12.5 10l1.5 1.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  ),
  /** Sliders — parameter / configuration panels */
  Sliders: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M2 4h12M2 8h12M2 12h12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
      <rect x="4" y="2.5" width="2.5" height="3" rx="1.25" fill="currentColor"/>
      <rect x="9" y="6.5" width="2.5" height="3" rx="1.25" fill="currentColor"/>
      <rect x="5.5" y="10.5" width="2.5" height="3" rx="1.25" fill="currentColor"/>
    </svg>
  ),
  /** Layers — stacked modules / priority order */
  Layers: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M8 2L2 5.5 8 9l6-3.5L8 2Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round"/>
      <path d="M2 9l6 3.5L14 9" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
      <path d="M2 12l6 3.5L14 12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" opacity=".45"/>
    </svg>
  ),
  /** Terminal — debug / console / logging */
  Terminal: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <rect x="1.5" y="2.5" width="13" height="11" rx="1.5" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M4 6l3 3-3 3" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
      <path d="M8.5 12H12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  ),
  /** Radar — detection / awareness modules */
  Radar: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <circle cx="8" cy="8" r="6.5" stroke="currentColor" strokeWidth="1.5"/>
      <circle cx="8" cy="8" r="3.5" stroke="currentColor" strokeWidth="1.5" opacity=".45"/>
      <path d="M8 8L12 4" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
      <circle cx="11" cy="5" r="1" fill="currentColor"/>
      <circle cx="8" cy="8" r="1" fill="currentColor"/>
    </svg>
  ),
  /** Lock — restricted / disabled state */
  Lock: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <rect x="3.5" y="7.5" width="9" height="7" rx="1.5" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M5 7.5V5a3 3 0 016 0v2.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
      <circle cx="8" cy="11" r="1" fill="currentColor"/>
    </svg>
  ),
  /** Clock — tick rate / timing / cooldown */
  Clock: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <circle cx="8" cy="8" r="6.5" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M8 4.5V8l2.5 2.5" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
    </svg>
  ),
  /** Cpu — performance metrics / frame rate */
  Cpu: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <rect x="4.5" y="4.5" width="7" height="7" rx="1" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M6 4.5V2M8 4.5V2M10 4.5V2M6 11.5V14M8 11.5V14M10 11.5V14M4.5 6H2M4.5 8H2M4.5 10H2M11.5 6H14M11.5 8H14M11.5 10H14" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  ),
  /** Map — minimap / coordinates */
  Map: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M1.5 3.5l4 1.5 5-2 4 1.5v8L10.5 11l-5 2-4-1.5V3.5Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round"/>
      <path d="M5.5 5v8M10.5 3v8" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round"/>
    </svg>
  ),
  /** Grid — module grid view toggle */
  Grid: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <rect x="2" y="2" width="5" height="5" rx="1" stroke="currentColor" strokeWidth="1.5"/>
      <rect x="9" y="2" width="5" height="5" rx="1" stroke="currentColor" strokeWidth="1.5"/>
      <rect x="2" y="9" width="5" height="5" rx="1" stroke="currentColor" strokeWidth="1.5"/>
      <rect x="9" y="9" width="5" height="5" rx="1" stroke="currentColor" strokeWidth="1.5"/>
    </svg>
  ),
  /** Bell — notification / alert */
  Bell: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <path d="M8 2.5a4 4 0 014 4V10l1.5 2h-11L4 10V6.5a4 4 0 014-4Z" stroke="currentColor" strokeWidth="1.5" strokeLinejoin="round"/>
      <path d="M6.5 12a1.5 1.5 0 003 0" stroke="currentColor" strokeWidth="1.5"/>
    </svg>
  ),
  /** Database — config storage / profile data */
  Database: ({ size = 16, className = '' }: IconProps) => (
    <svg width={size} height={size} viewBox="0 0 16 16" fill="none" className={className} aria-hidden>
      <ellipse cx="8" cy="4.5" rx="5.5" ry="2" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M2.5 4.5V8c0 1.1 2.46 2 5.5 2s5.5-.9 5.5-2V4.5" stroke="currentColor" strokeWidth="1.5"/>
      <path d="M2.5 8v3.5c0 1.1 2.46 2 5.5 2s5.5-.9 5.5-2V8" stroke="currentColor" strokeWidth="1.5"/>
    </svg>
  ),
}

// Icon metadata for the design system page
const ICON_CATALOG = [
  { name: 'Shield',     component: Icon.Shield,     usage: 'Protection / anti-cheat' },
  { name: 'Crosshair',  component: Icon.Crosshair,  usage: 'Targeting / aim modules' },
  { name: 'Eye',        component: Icon.Eye,         usage: 'Visibility / ESP / render' },
  { name: 'Zap',        component: Icon.Zap,         usage: 'Speed / performance' },
  { name: 'Key',        component: Icon.Key,         usage: 'Keybind configuration' },
  { name: 'Sliders',    component: Icon.Sliders,     usage: 'Parameter panels' },
  { name: 'Layers',     component: Icon.Layers,      usage: 'Module priority / stack' },
  { name: 'Terminal',   component: Icon.Terminal,    usage: 'Debug / console / logs' },
  { name: 'Radar',      component: Icon.Radar,       usage: 'Detection / awareness' },
  { name: 'Lock',       component: Icon.Lock,        usage: 'Restricted / disabled state' },
  { name: 'Clock',      component: Icon.Clock,       usage: 'Tick rate / cooldown' },
  { name: 'Cpu',        component: Icon.Cpu,         usage: 'Performance metrics / FPS' },
  { name: 'Map',        component: Icon.Map,         usage: 'Minimap / coordinates' },
  { name: 'Grid',       component: Icon.Grid,        usage: 'Module grid view' },
  { name: 'Bell',       component: Icon.Bell,        usage: 'Notification / alert' },
  { name: 'Database',   component: Icon.Database,    usage: 'Config storage / profiles' },
]

// ─────────────────────────────────────────────
// MOCK DATA
// ─────────────────────────────────────────────
const CATEGORIES = ['CATEGORY 01', 'CATEGORY 02', 'CATEGORY 03', 'CATEGORY 04', 'CATEGORY 05', 'CATEGORY 06']
const MODULES = [
  { id: 'mod_a', name: 'MODULE A', desc: 'Structural placeholder module demonstrating standard card layout and interactive states.', enabled: true },
  { id: 'mod_b', name: 'MODULE B', desc: 'Generic descriptive text for module capability. Defines scope and limitations.', enabled: false },
  { id: 'mod_c', name: 'MODULE C', desc: 'Extended feature set placeholder with configurable parameters.', enabled: true },
  { id: 'mod_d', name: 'MODULE D', desc: 'Core system component demonstrating disabled states.', enabled: false, disabled: true },
  { id: 'mod_e', name: 'MODULE E', desc: 'Supplementary structural module for grid population.', enabled: false },
]

// ─────────────────────────────────────────────
// PRIMITIVE COMPONENTS
// ─────────────────────────────────────────────
function IconButton({ icon: Ic, active, onClick, disabled }: any) {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`p-1.5 rounded-[2px] transition-colors border ${active ? 'bg-neutral-800 border-neutral-600 text-white' : 'bg-transparent border-transparent text-neutral-400 hover:text-white hover:bg-neutral-800 hover:border-neutral-700'} ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}`}
    >
      <Ic size={14} />
    </button>
  )
}

function Btn({ children, variant = 'default', active, className = '', onClick }: any) {
  const base = "px-3 py-1.5 rounded-[2px] text-[11px] font-medium tracking-wide transition-all border font-mono cursor-pointer inline-flex items-center gap-1.5"
  const v: Record<string, string> = {
    default: "bg-neutral-900 border-neutral-700 text-neutral-300 hover:bg-neutral-800 hover:text-white hover:border-neutral-500",
    primary: "bg-neutral-200 border-neutral-200 text-neutral-900 hover:bg-white",
    ghost:   "bg-transparent border-transparent text-neutral-400 hover:text-neutral-200 hover:bg-neutral-800",
    danger:  "bg-red-950/30 border-red-900 text-red-400 hover:bg-red-900/50 hover:text-red-300",
    accent:  "bg-green-950/60 border-green-900/70 text-green-400 hover:bg-green-950 hover:border-green-700",
  }
  return (
    <button onClick={onClick} className={`${base} ${v[variant]} ${active ? 'bg-neutral-800 text-white border-neutral-500' : ''} ${className}`}>
      {children}
    </button>
  )
}

function Toggle({ enabled, onToggle }: { enabled: boolean; onToggle?: () => void }) {
  return (
    <button
      onClick={onToggle}
      className={`relative flex items-center w-8 h-4 rounded-[2px] transition-colors border cursor-pointer ${enabled ? 'bg-green-950/40 border-green-800' : 'bg-neutral-900 border-neutral-700'}`}
    >
      <div className={`absolute top-0.5 bottom-0.5 w-3 rounded-[1px] transition-all ${enabled ? 'bg-green-500 left-4' : 'bg-neutral-500 left-0.5'}`} />
    </button>
  )
}

function Slider({ label, value }: { label: string; value: number }) {
  return (
    <div className="flex flex-col gap-1.5 w-full">
      <div className="flex justify-between text-[10px] text-neutral-400 uppercase tracking-widest font-mono">
        <span>{label}</span>
        <span className="text-neutral-200 tabular-nums">{value}%</span>
      </div>
      <div className="h-1.5 bg-neutral-900 border border-neutral-800 rounded-[1px] relative">
        <div className="absolute top-0 bottom-0 left-0 bg-neutral-300 rounded-[1px]" style={{ width: `${value}%` }} />
        <div className="absolute top-1/2 -translate-y-1/2 w-2 h-3 bg-white shadow-sm rounded-[1px]" style={{ left: `calc(${value}% - 4px)` }} />
      </div>
    </div>
  )
}

function Dropdown({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex flex-col gap-1.5 w-full">
      <div className="text-[10px] text-neutral-400 uppercase tracking-widest font-mono">{label}</div>
      <div className="flex justify-between items-center px-2 py-1.5 bg-neutral-900 border border-neutral-700 rounded-[2px] text-[11px] text-neutral-200 cursor-pointer hover:border-neutral-500 transition-colors">
        <span className="font-mono">{value}</span>
        <ChevronRight size={12} className="rotate-90 opacity-50" />
      </div>
    </div>
  )
}

function Checkbox({ label, checked }: { label: string; checked: boolean }) {
  return (
    <div className="flex items-center gap-2 cursor-pointer group">
      <div className={`w-3.5 h-3.5 rounded-[1px] border flex items-center justify-center transition-colors ${checked ? 'bg-neutral-300 border-neutral-300 text-neutral-900' : 'bg-neutral-900 border-neutral-700 text-transparent group-hover:border-neutral-500'}`}>
        <Check size={10} />
      </div>
      <span className="text-[11px] text-neutral-300 font-mono tracking-wide">{label}</span>
    </div>
  )
}

function Keybind({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex items-center justify-between w-full">
      <span className="text-[11px] text-neutral-400 font-mono uppercase tracking-widest">{label}</span>
      <div className="px-2 py-1 bg-neutral-900 border border-neutral-700 rounded-[2px] text-[10px] text-neutral-200 font-mono tracking-widest min-w-[60px] text-center hover:border-neutral-400 transition-colors cursor-pointer">
        {value}
      </div>
    </div>
  )
}

function ModuleCard({ data, onSelect }: any) {
  return (
    <div className={`p-3 border rounded-[2px] transition-all bg-neutral-950/80 backdrop-blur-md flex flex-col gap-2 ${data.disabled ? 'opacity-50' : 'hover:border-neutral-600 cursor-pointer'} ${data.enabled ? 'border-green-900/50' : 'border-neutral-800'}`}>
      <div className="flex items-start justify-between">
        <div className="flex items-center gap-2">
          <div className={`w-1.5 h-1.5 rounded-full ${data.enabled ? 'bg-green-500 shadow-[0_0_8px_rgba(74,222,128,0.6)]' : 'bg-neutral-700'}`} />
          <span className="text-[12px] font-bold text-neutral-100 font-mono tracking-wide">{data.name}</span>
        </div>
        <div className="flex items-center gap-1.5">
          <IconButton icon={Settings} onClick={onSelect} disabled={data.disabled} />
          <Toggle enabled={data.enabled} />
        </div>
      </div>
      <div className="text-[10px] text-neutral-400 leading-relaxed max-w-[90%] font-mono">{data.desc}</div>
    </div>
  )
}

// ─────────────────────────────────────────────
// DESIGN SYSTEM PAGE
// ─────────────────────────────────────────────
function DesignSystemPage() {
  const Section = ({ title, children }: { title: string; children: React.ReactNode }) => (
    <section className="mb-12">
      <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
        <span>{title}</span>
        <div className="flex-1 h-px bg-neutral-800" />
      </div>
      {children}
    </section>
  )

  // Typography specimens
  const typeScale = [
    { name: 'HERO',        size: '28px', tracking: '0.25em', weight: '700', family: 'JetBrains Mono', rule: 'Brand moments only. Max 1 per view.' },
    { name: 'DISPLAY',     size: '20px', tracking: '0.20em', weight: '700', family: 'JetBrains Mono', rule: 'View / section headings.' },
    { name: 'HEADING',     size: '16px', tracking: '0.15em', weight: '700', family: 'JetBrains Mono', rule: 'Panel titles, card headings.' },
    { name: 'SUBHEADING',  size: '13px', tracking: '0.08em', weight: '600', family: 'JetBrains Mono', rule: 'Secondary headings, breadcrumbs.' },
    { name: 'DEFAULT',     size: '12px', tracking: '0.02em', weight: '400', family: 'JetBrains Mono', rule: 'Default UI text.' },
    { name: 'BODY',        size: '11px', tracking: '0.04em', weight: '400', family: 'JetBrains Mono', rule: 'Descriptions, values, module text.' },
    { name: 'LABEL',       size: '10px', tracking: '0.12em', weight: '500', family: 'JetBrains Mono', rule: 'Field labels, section dividers.' },
    { name: 'MICRO',       size: '9px',  tracking: '0.15em', weight: '400', family: 'JetBrains Mono', rule: 'Metadata, timestamps, footers.' },
  ]

  const colorGroups = [
    {
      label: 'BACKGROUNDS',
      tokens: [
        { name: '--bg-0', value: '#060606', label: 'App root' },
        { name: '--bg-1', value: '#0E0E0E', label: 'Shell body' },
        { name: '--bg-2', value: '#161616', label: 'Panel surface' },
        { name: '--bg-3', value: '#1E1E1E', label: 'Sidebar' },
        { name: '--bg-4', value: '#272727', label: 'Input / card' },
        { name: '--bg-5', value: '#303030', label: 'Hover' },
      ],
    },
    {
      label: 'BORDERS',
      tokens: [
        { name: '--bd-0', value: '#181818', label: 'Hairline' },
        { name: '--bd-1', value: '#242424', label: 'Default' },
        { name: '--bd-2', value: '#333333', label: 'Emphasis' },
        { name: '--bd-3', value: '#444444', label: 'Focus' },
      ],
    },
    {
      label: 'TEXT',
      tokens: [
        { name: '--tx-0', value: '#EFEFEF', label: 'Primary' },
        { name: '--tx-1', value: '#A0A0A0', label: 'Secondary' },
        { name: '--tx-2', value: '#5C5C5C', label: 'Tertiary' },
        { name: '--tx-3', value: '#333333', label: 'Disabled' },
      ],
    },
    {
      label: 'ACCENT & SEMANTIC',
      tokens: [
        { name: '--ac',      value: '#4ADE80', label: 'Emerald — primary accent' },
        { name: '--success', value: '#4ADE80', label: 'Success / enabled' },
        { name: '--warning', value: '#FCD34D', label: 'Warning / caution' },
        { name: '--danger',  value: '#F87171', label: 'Error / destructive' },
        { name: '--info',    value: '#60A5FA', label: 'Info / neutral status' },
        { name: '--purple',  value: '#A78BFA', label: 'Special / premium' },
        { name: '--orange',  value: '#FB923C', label: 'Alert / override' },
      ],
    },
  ]

  const radiusTokens = [
    { name: '--r-1', value: '2px', label: 'Inputs, buttons, micro UI' },
    { name: '--r-2', value: '3px', label: 'Tags, badges' },
    { name: '--r-3', value: '4px', label: 'Cards, panels, shell window' },
    { name: '--r-4', value: '6px', label: 'Modals (reserved)' },
  ]

  const spacingTokens = [
    { name: '--sp-1', value: '4px' },
    { name: '--sp-2', value: '8px' },
    { name: '--sp-3', value: '12px' },
    { name: '--sp-4', value: '16px' },
    { name: '--sp-6', value: '24px' },
    { name: '--sp-8', value: '32px' },
    { name: '--sp-12', value: '48px' },
  ]

  return (
    <div className="h-full overflow-y-auto bg-neutral-950 text-neutral-200">
      <div className="max-w-[900px] mx-auto px-10 py-10">

        {/* Header */}
        <div className="mb-12 pb-8 border-b border-neutral-800">
          <div className="text-[9px] font-mono tracking-[0.2em] text-green-500 uppercase mb-3">Swiss × Game-HUD — Design Language v1.0</div>
          <h1 className="text-[28px] font-bold tracking-[0.25em] font-mono text-white mb-3">DESIGN SYSTEM</h1>
          <p className="text-[11px] text-neutral-400 font-mono leading-relaxed max-w-[520px]">
            Foundation tokens, typographic rules, icon vectors, and visual grammar for the in-game client menu. All UI is derived from this single source.
          </p>
          <div className="flex gap-6 mt-5 text-[9px] font-mono text-neutral-600 tracking-widest uppercase">
            <span>JetBrains Mono — Primary typeface</span>
            <span className="text-neutral-800">·</span>
            <span>Inter — Prose / descriptions</span>
            <span className="text-neutral-800">·</span>
            <span>Press Start 2P — Brand mark only</span>
          </div>
        </div>

        {/* TYPOGRAPHY */}
        <Section title="Typography Scale">
          {/* Font family rules */}
          <div className="grid grid-cols-3 gap-3 mb-8">
            {[
              { name: 'JetBrains Mono', rule: 'ALL UI', desc: 'Navigation, labels, values, keybinds, numbers, module names, button text. Default to this everywhere.', w: '400 500 600 700', sample: 'CATEGORY 01', sampleSize: '14px' },
              { name: 'Inter', rule: 'PROSE', desc: 'Module descriptions, help text, tooltip body, any sentence-length text that needs to breathe.', w: '300 400 500', sample: 'Extended feature set for configurable parameters.', sampleSize: '12px' },
              { name: 'Press Start 2P', rule: 'BRAND ONLY', desc: 'Logo mark, version badge. Never use for navigation, labels, or any interactive element.', w: '400', sample: 'CLIENT', sampleSize: '12px' },
            ].map(f => (
              <div key={f.name} className="p-4 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
                <div className="text-[9px] font-mono tracking-[0.15em] text-green-500 uppercase mb-1">{f.rule}</div>
                <div className="text-[11px] font-mono text-neutral-300 mb-1">{f.name}</div>
                <div className="text-[9px] text-neutral-600 font-mono mb-3">Weights: {f.w}</div>
                <div className="text-neutral-400 border-t border-neutral-800 pt-3 leading-relaxed" style={{ fontFamily: f.name === 'JetBrains Mono' ? '"JetBrains Mono", monospace' : f.name === 'Inter' ? '"Inter", sans-serif' : '"Press Start 2P", monospace', fontSize: f.sampleSize }}>
                  {f.sample}
                </div>
                <div className="text-[9px] text-neutral-600 mt-3 leading-relaxed font-mono">{f.desc}</div>
              </div>
            ))}
          </div>

          {/* Type scale table */}
          <div className="border border-neutral-800 rounded-[2px] overflow-hidden">
            <div className="grid grid-cols-[100px_60px_80px_60px_1fr] text-[9px] font-mono tracking-[0.12em] text-neutral-600 uppercase px-4 py-2 bg-neutral-900/40 border-b border-neutral-800">
              <span>Role</span><span>Size</span><span>Tracking</span><span>Weight</span><span>Rule</span>
            </div>
            {typeScale.map((t, i) => (
              <div key={t.name} className={`grid grid-cols-[100px_60px_80px_60px_1fr] items-center px-4 py-3 ${i % 2 === 0 ? 'bg-neutral-950' : 'bg-neutral-900/20'}`}>
                <span className="text-[9px] font-mono text-neutral-500 tracking-[0.1em]">{t.name}</span>
                <span className="text-[9px] font-mono text-neutral-400 tabular-nums">{t.size}</span>
                <span className="text-[9px] font-mono text-neutral-500 tabular-nums">{t.tracking}</span>
                <span className="text-[9px] font-mono text-neutral-500">{t.weight}</span>
                <span className="text-neutral-100 font-mono" style={{ fontSize: t.size, letterSpacing: t.tracking, fontWeight: t.weight }}>
                  {t.name === 'HERO' ? 'CLIENT IDENTITY' : t.name === 'DISPLAY' ? 'CATEGORY 01' : t.name === 'HEADING' ? 'MODULE A' : t.name === 'SUBHEADING' ? 'Configuration' : t.name === 'LABEL' ? 'RENDER DISTANCE' : t.name === 'MICRO' ? 'v1.0.0-rc · 2026-09-21' : 'Generic module description text.'}
                </span>
              </div>
            ))}
          </div>
          <div className="mt-3 flex flex-col gap-1">
            {[
              'Never mix tracking values within one text element.',
              'Use tabular-nums (tabular figures) for any numeric value that updates or aligns in a column.',
              'All caps (uppercase) labels require tracking ≥ 0.10em. Never all-caps body text.',
              'Line-height: 1.0–1.2 for headings / display. 1.4–1.6 for body / label. Never auto.',
            ].map(r => (
              <div key={r} className="flex items-start gap-2 text-[9px] font-mono text-neutral-600">
                <span className="text-green-700 mt-0.5">▸</span>
                <span>{r}</span>
              </div>
            ))}
          </div>
        </Section>

        {/* COLORS */}
        <Section title="Color Tokens">
          {colorGroups.map(group => (
            <div key={group.label} className="mb-6">
              <div className="text-[9px] font-mono text-neutral-600 tracking-[0.12em] uppercase mb-2">{group.label}</div>
              <div className="flex flex-wrap gap-2">
                {group.tokens.map(tok => (
                  <div key={tok.name} className="flex items-center gap-2.5 px-3 py-2 bg-neutral-900 border border-neutral-800 rounded-[2px] min-w-[200px]">
                    <div className="w-6 h-6 rounded-[2px] border border-white/10 shrink-0" style={{ backgroundColor: tok.value }} />
                    <div>
                      <div className="text-[9px] font-mono text-neutral-400 tracking-wide">{tok.name}</div>
                      <div className="text-[9px] font-mono text-neutral-600">{tok.value} · {tok.label}</div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </Section>

        {/* SPACING */}
        <Section title="Spacing Scale">
          <div className="flex items-end gap-3 flex-wrap">
            {spacingTokens.map(sp => {
              const px = parseInt(sp.value)
              return (
                <div key={sp.name} className="flex flex-col items-center gap-2">
                  <div className="bg-green-500/20 border border-green-800/50" style={{ width: px, height: px, minWidth: px, minHeight: px }} />
                  <div className="text-[9px] font-mono text-neutral-500 text-center">{sp.value}</div>
                  <div className="text-[8px] font-mono text-neutral-700 text-center">{sp.name}</div>
                </div>
              )
            })}
          </div>
        </Section>

        {/* RADIUS */}
        <Section title="Border Radius">
          <div className="flex gap-4 flex-wrap">
            {radiusTokens.map(r => (
              <div key={r.name} className="p-4 bg-neutral-900/50 border border-neutral-800 flex flex-col gap-2 min-w-[180px]" style={{ borderRadius: r.value }}>
                <div className="text-[9px] font-mono text-neutral-500">{r.name} — {r.value}</div>
                <div className="text-[9px] font-mono text-neutral-600 leading-relaxed">{r.label}</div>
              </div>
            ))}
          </div>
          <div className="mt-3 text-[9px] font-mono text-neutral-600">
            Rule: prefer <span className="text-neutral-400">--r-1</span> and <span className="text-neutral-400">--r-3</span> for all interactive elements. Reserve <span className="text-neutral-400">--r-4</span> for future modal overlays only.
          </div>
        </Section>

        {/* ICONS */}
        <Section title="Icon System">
          <div className="mb-4 text-[9px] font-mono text-neutral-600 leading-relaxed max-w-[600px]">
            All icons are 16×16 viewport, 1.5px stroke, round line caps, drawn on a pixel grid for HUD legibility at 12–20px. Use <span className="text-neutral-400">currentColor</span> exclusively — no hardcoded fills. Always render at multiples of 4px (12, 16, 20, 24). Import from <span className="text-neutral-400">Icon.Name</span>.
          </div>
          <div className="grid grid-cols-4 gap-2">
            {ICON_CATALOG.map(({ name, component: Ic, usage }) => (
              <div key={name} className="flex items-center gap-3 px-3 py-2.5 bg-neutral-900/50 border border-neutral-800 rounded-[2px] hover:border-neutral-600 transition-colors group">
                <div className="w-8 h-8 rounded-[2px] bg-neutral-800 border border-neutral-700 flex items-center justify-center text-neutral-300 group-hover:text-white group-hover:border-neutral-600 transition-colors shrink-0">
                  <Ic size={16} />
                </div>
                <div>
                  <div className="text-[10px] font-mono text-neutral-200 tracking-wide">Icon.{name}</div>
                  <div className="text-[9px] font-mono text-neutral-600 leading-snug">{usage}</div>
                </div>
              </div>
            ))}
          </div>

          {/* Icon size demos */}
          <div className="mt-4 p-4 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
            <div className="text-[9px] font-mono text-neutral-600 uppercase tracking-widest mb-3">Size scale — always multiples of 4px</div>
            <div className="flex items-end gap-6">
              {[12, 16, 20, 24].map(s => (
                <div key={s} className="flex flex-col items-center gap-2">
                  <div className="text-neutral-400"><Icon.Crosshair size={s} /></div>
                  <div className="text-[9px] font-mono text-neutral-600">{s}px</div>
                </div>
              ))}
            </div>
          </div>
        </Section>

        {/* SHADOW / ELEVATION */}
        <Section title="Elevation & Shadow">
          <div className="grid grid-cols-4 gap-3">
            {[
              { name: '--shadow-sm',    value: '0 1px 3px rgba(0,0,0,0.4)',  label: 'Inline UI elements' },
              { name: '--shadow-md',    value: '0 4px 12px rgba(0,0,0,0.5)', label: 'Floating labels / hints' },
              { name: '--shadow-lg',    value: '0 8px 24px rgba(0,0,0,0.7)', label: 'Shell window, panels' },
              { name: '--shadow-panel', value: 'inset 0 1px 0 rgba(255,255,255,0.03)', label: 'Inner top-edge shimmer' },
            ].map(sh => (
              <div key={sh.name} className="p-4 bg-neutral-900 border border-neutral-800 rounded-[2px] flex flex-col gap-2" style={{ boxShadow: sh.value }}>
                <div className="text-[9px] font-mono text-neutral-400">{sh.name}</div>
                <div className="text-[9px] font-mono text-neutral-600 leading-relaxed">{sh.label}</div>
              </div>
            ))}
          </div>
        </Section>

        {/* MOTION */}
        <Section title="Motion & Transitions">
          <div className="grid grid-cols-2 gap-3">
            {[
              { name: 'Micro', duration: '80ms', easing: 'ease', use: 'Toggle, checkbox, icon color' },
              { name: 'Snap',  duration: '120ms', easing: 'ease', use: 'Button states, border colors' },
              { name: 'Slide', duration: '200ms', easing: 'ease-out', use: 'View transitions, slide-in panels' },
              { name: 'Fade',  duration: '200ms', easing: 'ease', use: 'Page / tab content transitions' },
            ].map(m => (
              <div key={m.name} className="flex justify-between items-center px-3 py-2.5 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
                <div>
                  <div className="text-[10px] font-mono text-neutral-300">{m.name}</div>
                  <div className="text-[9px] font-mono text-neutral-600">{m.use}</div>
                </div>
                <div className="text-right">
                  <div className="text-[10px] font-mono text-neutral-400 tabular-nums">{m.duration}</div>
                  <div className="text-[9px] font-mono text-neutral-600">{m.easing}</div>
                </div>
              </div>
            ))}
          </div>
          <div className="mt-3 text-[9px] font-mono text-neutral-600">
            Rule: all transitions are <span className="text-neutral-400">color / background-color / border-color / opacity</span> only. Never animate layout properties (width, height, padding) — use opacity + transform instead.
          </div>
        </Section>

      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// COMPONENTS PAGE
// ─────────────────────────────────────────────
function ComponentsPage() {
  const [toggleA, setToggleA] = useState(true)
  const [toggleB, setToggleB] = useState(false)
  const [sliderVal] = useState(65)

  const DemoSection = ({ title, children }: { title: string; children: React.ReactNode }) => (
    <section className="mb-10">
      <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
        <span>{title}</span>
        <div className="flex-1 h-px bg-neutral-800" />
      </div>
      {children}
    </section>
  )

  const DemoRow = ({ label, children }: { label: string; children: React.ReactNode }) => (
    <div className="flex items-center gap-6 py-3 border-b border-neutral-900">
      <div className="w-32 text-[9px] font-mono text-neutral-600 uppercase tracking-widest shrink-0">{label}</div>
      <div className="flex items-center gap-3 flex-wrap">{children}</div>
    </div>
  )

  return (
    <div className="h-full overflow-y-auto bg-neutral-950 text-neutral-200">
      <div className="max-w-[880px] mx-auto px-10 py-10">

        <div className="mb-10 pb-8 border-b border-neutral-800">
          <div className="text-[9px] font-mono tracking-[0.2em] text-green-500 uppercase mb-3">Interactive component catalogue</div>
          <h1 className="text-[20px] font-bold tracking-[0.2em] font-mono text-white">COMPONENTS</h1>
        </div>

        {/* BUTTONS */}
        <DemoSection title="Button">
          <DemoRow label="Variants">
            <Btn variant="default">DEFAULT</Btn>
            <Btn variant="primary">PRIMARY</Btn>
            <Btn variant="ghost">GHOST</Btn>
            <Btn variant="danger">DANGER</Btn>
            <Btn variant="accent"><Icon.Zap size={12} /> ACCENT</Btn>
          </DemoRow>
          <DemoRow label="With icons">
            <Btn variant="default"><Icon.Sliders size={12} /> CONFIGURE</Btn>
            <Btn variant="default"><Icon.Key size={12} /> BIND KEY</Btn>
            <Btn variant="danger"><RotateCcw size={12} /> RESET MODULE</Btn>
            <Btn variant="accent"><Icon.Shield size={12} /> ENABLE</Btn>
          </DemoRow>
          <DemoRow label="States">
            <Btn variant="default" active>ACTIVE</Btn>
            <Btn variant="default" className="opacity-50 cursor-not-allowed">DISABLED</Btn>
          </DemoRow>
        </DemoSection>

        {/* TOGGLE */}
        <DemoSection title="Toggle">
          <DemoRow label="On / Off">
            <Toggle enabled={toggleA} onToggle={() => setToggleA(v => !v)} />
            <Toggle enabled={toggleB} onToggle={() => setToggleB(v => !v)} />
            <div className="text-[9px] font-mono text-neutral-600 ml-2">(click to toggle)</div>
          </DemoRow>
          <DemoRow label="With label">
            {[true, false].map(s => (
              <div key={String(s)} className="flex items-center gap-3 px-3 py-2 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
                <Toggle enabled={s} />
                <span className={`text-[11px] font-mono tracking-widest ${s ? 'text-green-400' : 'text-neutral-500'}`}>{s ? 'ENABLED' : 'DISABLED'}</span>
              </div>
            ))}
          </DemoRow>
        </DemoSection>

        {/* SLIDER */}
        <DemoSection title="Slider">
          <div className="grid grid-cols-2 gap-6 py-2">
            <Slider label="INTENSITY THRESHOLD" value={sliderVal} />
            <Slider label="RENDER DISTANCE" value={40} />
            <Slider label="TICK RATE" value={90} />
            <Slider label="OPACITY" value={15} />
          </div>
        </DemoSection>

        {/* DROPDOWN */}
        <DemoSection title="Dropdown">
          <div className="grid grid-cols-2 gap-6 py-2">
            <Dropdown label="RENDER MODE" value="PERFORMANCE" />
            <Dropdown label="UPDATE FREQUENCY" value="TICK-BASED" />
            <Dropdown label="THEME ACCENT" value="DEFAULT (GREEN)" />
            <Dropdown label="GUI SCALE" value="AUTO" />
          </div>
        </DemoSection>

        {/* CHECKBOX */}
        <DemoSection title="Checkbox">
          <div className="grid grid-cols-2 gap-3 py-2">
            <Checkbox label="ENABLE DIAGNOSTICS" checked={true} />
            <Checkbox label="OVERRIDE DEFAULT KEYBINDS" checked={false} />
            <Checkbox label="FORCE NATIVE RESOLUTION" checked={true} />
            <Checkbox label="EXPERIMENTAL FEATURES" checked={false} />
            <Checkbox label="ANONYMOUS TELEMETRY" checked={true} />
            <Checkbox label="AUTO-UPDATE MODULES" checked={false} />
          </div>
        </DemoSection>

        {/* KEYBIND */}
        <DemoSection title="Keybind">
          <div className="flex flex-col gap-2 py-2 max-w-[380px]">
            <Keybind label="PRIMARY ACTION" value="MOUSE 4" />
            <Keybind label="SECONDARY ACTION" value="LSHIFT" />
            <Keybind label="TOGGLE MODULE" value="UNBOUND" />
            <Keybind label="OPEN CLIENT" value="RCTRL" />
          </div>
        </DemoSection>

        {/* MODULE CARD */}
        <DemoSection title="Module Card">
          <div className="grid grid-cols-2 gap-3">
            {MODULES.map(m => <ModuleCard key={m.id} data={m} onSelect={() => {}} />)}
          </div>
        </DemoSection>

        {/* ICON BUTTONS */}
        <DemoSection title="Icon Button">
          <DemoRow label="States">
            <IconButton icon={Settings} active={false} />
            <IconButton icon={Settings} active={true} />
            <IconButton icon={Settings} disabled={true} />
            <IconButton icon={X} active={false} />
          </DemoRow>
          <DemoRow label="Client icons">
            {[Icon.Shield, Icon.Eye, Icon.Crosshair, Icon.Radar, Icon.Terminal, Icon.Layers].map((Ic, i) => (
              <IconButton key={i} icon={Ic} active={false} />
            ))}
          </DemoRow>
        </DemoSection>

        {/* STATUS BADGES */}
        <DemoSection title="Status Badges">
          <div className="flex flex-wrap gap-2 py-2">
            {[
              { label: 'ACTIVE',    color: 'text-green-400 bg-green-950/40 border-green-900/60' },
              { label: 'DISABLED',  color: 'text-neutral-500 bg-neutral-900 border-neutral-800' },
              { label: 'ERROR',     color: 'text-red-400 bg-red-950/40 border-red-900/60' },
              { label: 'WARNING',   color: 'text-yellow-400 bg-yellow-950/40 border-yellow-900/60' },
              { label: 'INFO',      color: 'text-blue-400 bg-blue-950/40 border-blue-900/60' },
              { label: 'LOCKED',    color: 'text-purple-400 bg-purple-950/40 border-purple-900/60' },
            ].map(b => (
              <div key={b.label} className={`flex items-center gap-1.5 px-2.5 py-1 rounded-[2px] border text-[9px] font-mono tracking-[0.12em] ${b.color}`}>
                <div className={`w-1.5 h-1.5 rounded-full ${b.label === 'ACTIVE' ? 'bg-green-500 shadow-[0_0_6px_rgba(74,222,128,0.6)]' : b.label === 'ERROR' ? 'bg-red-500' : b.label === 'WARNING' ? 'bg-yellow-500' : b.label === 'INFO' ? 'bg-blue-500' : b.label === 'LOCKED' ? 'bg-purple-500' : 'bg-neutral-600'}`} />
                {b.label}
              </div>
            ))}
          </div>
        </DemoSection>

        {/* DIVIDERS */}
        <DemoSection title="Section Dividers">
          <div className="flex flex-col gap-4 py-2">
            <div className="flex items-center gap-3">
              <div className="flex-1 h-px bg-neutral-800" />
            </div>
            <div className="flex items-center gap-3">
              <span className="text-[9px] font-mono tracking-[0.15em] text-neutral-600 uppercase">Section Label</span>
              <div className="flex-1 h-px bg-neutral-800" />
            </div>
            <div className="text-[10px] font-mono text-neutral-500 uppercase tracking-widest border-b border-neutral-800 pb-2">Configuration Parameters</div>
          </div>
        </DemoSection>

        {/* DATA DISPLAY */}
        <DemoSection title="Data Display">
          <div className="grid grid-cols-4 gap-3 py-2">
            {[
              { label: 'FPS', value: '247', unit: 'frames/s', color: 'text-green-400' },
              { label: 'PING', value: '18', unit: 'ms', color: 'text-green-400' },
              { label: 'MODULES', value: '12 / 32', unit: 'active', color: 'text-neutral-300' },
              { label: 'UPTIME', value: '03:42:11', unit: 'hh:mm:ss', color: 'text-neutral-300' },
            ].map(d => (
              <div key={d.label} className="p-3 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
                <div className="text-[9px] font-mono text-neutral-600 tracking-widest uppercase mb-1">{d.label}</div>
                <div className={`text-[20px] font-bold font-mono tabular-nums leading-none ${d.color}`}>{d.value}</div>
                <div className="text-[9px] font-mono text-neutral-600 mt-1">{d.unit}</div>
              </div>
            ))}
          </div>
        </DemoSection>

      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// PROTOTYPE VIEWS
// ─────────────────────────────────────────────
function GenericView({ activeTab, onSelectModule }: any) {
  return (
    <div className="h-full flex flex-col p-6 animate-in fade-in duration-200">
      <div className="mb-5">
        <h2 className="text-[20px] font-bold text-white tracking-[0.2em] font-mono">{activeTab}</h2>
        <p className="text-[10px] text-neutral-500 font-mono mt-1 uppercase tracking-widest">Generic module directory and configuration index.</p>
      </div>
      <div className="grid grid-cols-2 gap-3 auto-rows-max">
        {MODULES.map(m => <ModuleCard key={m.id} data={m} onSelect={() => onSelectModule(m)} />)}
      </div>
    </div>
  )
}

function ModuleDetailView({ module, onBack }: any) {
  return (
    <div className="h-full flex flex-col p-6 animate-in slide-in-from-right-4 duration-200">
      <div className="flex items-center gap-2 mb-5 text-[10px] font-mono tracking-widest">
        <button onClick={onBack} className="text-neutral-500 hover:text-white transition-colors cursor-pointer">DIRECTORY</button>
        <ChevronRight size={12} className="text-neutral-700" />
        <span className="text-white font-bold">{module.name}</span>
      </div>

      <div className="flex gap-8 flex-1">
        <div className="w-1/3 flex flex-col gap-3">
          <div className="p-4 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
            <div className="flex items-center justify-between mb-3">
              <span className="text-[9px] text-neutral-500 uppercase tracking-widest font-mono">Status</span>
              <Toggle enabled={module.enabled} />
            </div>
            <div className="text-[10px] text-neutral-500 uppercase tracking-widest font-mono mb-2">Description</div>
            <p className="text-[10px] text-neutral-400 leading-relaxed font-mono">{module.desc}</p>
          </div>
          <Btn variant="danger" className="w-full justify-center">
            <RotateCcw size={12} /> RESET MODULE
          </Btn>
        </div>

        <div className="flex-1 flex flex-col gap-5">
          <div className="text-[9px] text-neutral-600 uppercase tracking-widest font-mono border-b border-neutral-800 pb-2">Configuration Parameters</div>
          <div className="grid grid-cols-2 gap-x-8 gap-y-5">
            <Slider label="INTENSITY THRESHOLD" value={75} />
            <Slider label="RENDER DISTANCE" value={40} />
            <Dropdown label="RENDER MODE" value="PERFORMANCE" />
            <Dropdown label="UPDATE FREQUENCY" value="TICK-BASED" />
            <div className="col-span-2 grid grid-cols-2 gap-3 p-4 bg-neutral-900/30 border border-neutral-800 rounded-[2px]">
              <Checkbox label="ENABLE DIAGNOSTICS" checked={true} />
              <Checkbox label="OVERRIDE DEFAULT KEYBINDS" checked={false} />
              <Checkbox label="FORCE NATIVE RESOLUTION" checked={true} />
              <Checkbox label="EXPERIMENTAL FEATURES" checked={false} />
            </div>
            <div className="col-span-2 space-y-2.5">
              <div className="text-[9px] text-neutral-600 uppercase tracking-widest font-mono mb-1">Keybinds</div>
              <Keybind label="PRIMARY ACTION" value="MOUSE 4" />
              <Keybind label="SECONDARY ACTION" value="LSHIFT" />
              <Keybind label="TOGGLE MODULE" value="UNBOUND" />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function SettingsView() {
  const [telemetry, setTelemetry] = useState(true)
  return (
    <div className="h-full flex flex-col p-6 animate-in fade-in duration-200">
      <div className="mb-5">
        <h2 className="text-[20px] font-bold text-white tracking-[0.2em] font-mono">GLOBAL SETTINGS</h2>
        <p className="text-[10px] text-neutral-500 font-mono mt-1 uppercase tracking-widest">Client-wide configuration and environmental overrides.</p>
      </div>
      <div className="max-w-2xl flex flex-col gap-5">
        <div className="space-y-4">
          <div className="text-[9px] text-neutral-600 uppercase tracking-widest font-mono border-b border-neutral-800 pb-2">Appearance</div>
          <Slider label="GUI SCALE" value={80} />
          <Dropdown label="THEME ACCENT" value="DEFAULT (GREEN)" />
          <Checkbox label="ENABLE BLUR EFFECTS" checked={true} />
          <Checkbox label="SCANLINE OVERLAY" checked={false} />
        </div>
        <div className="space-y-4">
          <div className="text-[9px] text-neutral-600 uppercase tracking-widest font-mono border-b border-neutral-800 pb-2">Performance</div>
          <Slider label="TICK RATE LIMIT" value={100} />
          <Dropdown label="RENDER BACKEND" value="AUTO-DETECT" />
        </div>
        <div className="space-y-4">
          <div className="text-[9px] text-neutral-600 uppercase tracking-widest font-mono border-b border-neutral-800 pb-2">System</div>
          <div className="flex items-center gap-3">
            <Toggle enabled={telemetry} onToggle={() => setTelemetry(v => !v)} />
            <span className="text-[10px] text-neutral-400 font-mono">Anonymous telemetry {telemetry ? 'enabled' : 'disabled'}.</span>
          </div>
          <div className="flex gap-2">
            <Btn variant="default">EXPORT CONFIGURATION</Btn>
            <Btn variant="ghost">IMPORT</Btn>
            <Btn variant="danger">FACTORY RESET</Btn>
          </div>
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// GAME CLIENT SHELL
// ─────────────────────────────────────────────
function GameClientShell({ initialTab = CATEGORIES[0], showModule = null }: any) {
  const [activeTab, setActiveTab] = useState(initialTab)
  const [selectedModule, setSelectedModule] = useState(showModule)

  const handleTabChange = (tab: string) => {
    setActiveTab(tab)
    setSelectedModule(null)
  }

  const categoryIcons = [Icon.Shield, Icon.Eye, Icon.Crosshair, Icon.Zap, Icon.Radar, Icon.Layers]

  return (
    <div className="relative w-full h-full bg-neutral-950 flex items-center justify-center overflow-hidden select-none">
      {/* Gameplay background */}
      <div
        className="absolute inset-0 bg-cover bg-center opacity-40 scale-105"
        style={{ backgroundImage: 'url(https://images.unsplash.com/photo-1662531914405-c037eab676b4?q=80&w=1920)' }}
      />
      <div className="absolute inset-0 bg-black/60 backdrop-blur-[2px]" />

      {/* Client window */}
      <div className="relative w-[85%] h-[80%] max-w-[1200px] flex flex-col bg-neutral-950/90 border border-neutral-800/80 rounded-[4px] shadow-2xl overflow-hidden backdrop-blur-xl" style={{ boxShadow: '0 8px 48px rgba(0,0,0,0.8), inset 0 1px 0 rgba(255,255,255,0.04)' }}>

        {/* Header */}
        <header className="h-12 border-b border-neutral-800/80 flex items-center justify-between px-4 bg-neutral-900/50 shrink-0">
          <div className="flex items-center gap-4">
            <div className="w-6 h-6 bg-white rounded-[2px] flex items-center justify-center">
              <span className="text-black font-bold text-[12px] leading-none" style={{ fontFamily: '"Press Start 2P", monospace' }}>C</span>
            </div>
            <span className="text-[13px] font-bold tracking-[0.2em] text-white font-mono">CLIENT IDENTITY</span>
          </div>
          <div className="flex items-center gap-5">
            {/* Live stats */}
            <div className="flex items-center gap-4 text-[9px] font-mono tracking-widest text-neutral-600 tabular-nums">
              <span><span className="text-green-500">247</span> FPS</span>
              <span><span className="text-neutral-400">18</span> ms</span>
            </div>
            <div className="flex items-center gap-2 text-[10px] font-mono tracking-widest text-neutral-400 pl-4 border-l border-neutral-800">
              <span className="w-1.5 h-1.5 rounded-full bg-green-500 shadow-[0_0_8px_rgba(74,222,128,0.6)]" />
              SYSTEM ACTIVE
            </div>
            <div className="flex items-center gap-2 pl-4 border-l border-neutral-800">
              <div className="w-6 h-6 bg-neutral-800 rounded-[2px] border border-neutral-700 flex items-center justify-center overflow-hidden">
                <img src="https://api.dicebear.com/7.x/pixel-art/svg?seed=steve" alt="player avatar" className="w-full h-full opacity-80" />
              </div>
              <span className="text-[10px] font-mono tracking-widest text-neutral-300">PLAYER_1</span>
            </div>
          </div>
        </header>

        {/* Body */}
        <div className="flex flex-1 overflow-hidden">
          {/* Sidebar */}
          <aside className="w-[180px] border-r border-neutral-800/80 bg-neutral-900/20 flex flex-col shrink-0">
            <div className="flex-1 py-3 flex flex-col gap-0.5 px-2 overflow-y-auto">
              {CATEGORIES.map((cat, i) => {
                const CatIcon = categoryIcons[i]
                return (
                  <button
                    key={cat}
                    onClick={() => handleTabChange(cat)}
                    className={`w-full text-left px-3 py-2 rounded-[2px] text-[10px] font-mono uppercase tracking-widest transition-all flex items-center gap-2.5 cursor-pointer ${activeTab === cat ? 'bg-neutral-800 text-white shadow-inner' : 'text-neutral-500 hover:bg-neutral-800/50 hover:text-neutral-300'}`}
                  >
                    <CatIcon size={12} className={activeTab === cat ? 'text-green-400' : ''} />
                    {cat}
                  </button>
                )
              })}
            </div>
            <div className="p-2 border-t border-neutral-800/80">
              <button
                onClick={() => handleTabChange('SETTINGS')}
                className={`w-full flex items-center gap-2.5 px-3 py-2 rounded-[2px] text-[10px] font-mono uppercase tracking-widest transition-all cursor-pointer ${activeTab === 'SETTINGS' ? 'bg-neutral-800 text-white' : 'text-neutral-500 hover:bg-neutral-800/50 hover:text-neutral-300'}`}
              >
                <Icon.Sliders size={12} className={activeTab === 'SETTINGS' ? 'text-green-400' : ''} />
                SETTINGS
              </button>
            </div>
          </aside>

          {/* Main content */}
          <main className="flex-1 bg-gradient-to-br from-neutral-900/10 to-neutral-950/30 overflow-y-auto relative">
            {selectedModule ? (
              <ModuleDetailView module={selectedModule} onBack={() => setSelectedModule(null)} />
            ) : activeTab === 'SETTINGS' ? (
              <SettingsView />
            ) : (
              <GenericView activeTab={activeTab} onSelectModule={setSelectedModule} />
            )}
          </main>
        </div>

        {/* Footer */}
        <footer className="h-8 border-t border-neutral-800/80 bg-neutral-950 flex items-center px-4 shrink-0 justify-between text-[9px] font-mono text-neutral-600 tracking-widest uppercase">
          <div className="flex gap-5">
            <span>[ESC] CLOSE</span>
            <span>[↑↓] NAVIGATE</span>
            <span>[ENTER] SELECT</span>
            <span>[F1] KEYBINDS</span>
          </div>
          <div className="tabular-nums">v1.0.0-rc</div>
        </footer>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// STATIC SHELL FRAME — reusable chrome for state pages
// ─────────────────────────────────────────────
function ShellFrame({ children, label }: { children: React.ReactNode; label: string }) {
  return (
    <div className="flex flex-col gap-1.5">
      <div className="text-[9px] font-mono tracking-[0.15em] text-neutral-600 uppercase">{label}</div>
      <div className="relative flex flex-col bg-neutral-950/95 border border-neutral-800 rounded-[3px] overflow-hidden shadow-xl" style={{ boxShadow: '0 4px 24px rgba(0,0,0,0.7), inset 0 1px 0 rgba(255,255,255,0.03)' }}>
        {children}
      </div>
    </div>
  )
}

function StaticHeader() {
  return (
    <div className="h-8 border-b border-neutral-800/80 flex items-center justify-between px-3 bg-neutral-900/50 shrink-0">
      <div className="flex items-center gap-2">
        <div className="w-4 h-4 bg-white rounded-[1px] flex items-center justify-center">
          <span className="text-black font-bold text-[8px] leading-none" style={{ fontFamily: '"Press Start 2P", monospace' }}>C</span>
        </div>
        <span className="text-[9px] font-bold tracking-[0.2em] text-white font-mono">CLIENT IDENTITY</span>
      </div>
      <div className="flex items-center gap-3 text-[8px] font-mono tracking-widest text-neutral-600">
        <span><span className="text-green-500">247</span> FPS</span>
        <span className="w-1.5 h-1.5 rounded-full bg-green-500 shadow-[0_0_6px_rgba(74,222,128,0.5)]" />
      </div>
    </div>
  )
}

function StaticFooter() {
  return (
    <div className="h-6 border-t border-neutral-800/80 bg-neutral-950 flex items-center px-3 shrink-0 justify-between text-[8px] font-mono text-neutral-700 tracking-widest uppercase">
      <div className="flex gap-3"><span>[ESC] CLOSE</span><span>[↑↓] NAVIGATE</span><span>[ENTER] SELECT</span></div>
      <span className="tabular-nums">v1.0.0-rc</span>
    </div>
  )
}

// Static sidebar showing specified tab as active
function StaticSidebar({ activeTab }: { activeTab: string }) {
  const categoryIcons = [Icon.Shield, Icon.Eye, Icon.Crosshair, Icon.Zap, Icon.Radar, Icon.Layers]
  const isSettings = activeTab === 'SETTINGS'
  return (
    <div className="w-[120px] border-r border-neutral-800/80 bg-neutral-900/20 flex flex-col shrink-0">
      <div className="flex-1 py-2 flex flex-col gap-px px-1.5">
        {CATEGORIES.map((cat, i) => {
          const CatIcon = categoryIcons[i]
          const active = activeTab === cat
          return (
            <div key={cat} className={`w-full px-2 py-1.5 rounded-[2px] text-[8px] font-mono uppercase tracking-widest flex items-center gap-1.5 ${active ? 'bg-neutral-800 text-white' : 'text-neutral-600'}`}>
              <CatIcon size={10} className={active ? 'text-green-400' : ''} />
              {cat}
            </div>
          )
        })}
      </div>
      <div className="p-1.5 border-t border-neutral-800/80">
        <div className={`flex items-center gap-1.5 px-2 py-1.5 rounded-[2px] text-[8px] font-mono uppercase tracking-widest ${isSettings ? 'bg-neutral-800 text-white' : 'text-neutral-600'}`}>
          <Icon.Sliders size={10} className={isSettings ? 'text-green-400' : ''} />
          SETTINGS
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// NAVIGATION STATES PAGE
// ─────────────────────────────────────────────
function NavigationStatesPage() {
  const navStates = [
    { label: 'Default — Category 01 Active', active: 'CATEGORY 01' },
    { label: 'Mid Navigation — Category 04', active: 'CATEGORY 04' },
    { label: 'Settings Active', active: 'SETTINGS' },
  ]
  return (
    <div className="h-full overflow-y-auto bg-neutral-950 p-8">
      <div className="mb-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-green-500 uppercase mb-2">State documentation</div>
        <h1 className="text-[20px] font-bold tracking-[0.2em] font-mono text-white mb-1">NAVIGATION STATES</h1>
        <p className="text-[10px] text-neutral-500 font-mono">All sidebar navigation variants — active, default, settings — shown at a glance.</p>
      </div>

      {/* 3-up sidebar comparison */}
      <div className="grid grid-cols-3 gap-5 mb-10">
        {navStates.map(({ label, active }) => (
          <ShellFrame key={label} label={label}>
            <StaticHeader />
            <div className="flex" style={{ height: 260 }}>
              <StaticSidebar activeTab={active} />
              <div className="flex-1 flex items-center justify-center">
                <div className="text-[9px] font-mono text-neutral-800 tracking-widest uppercase">Content area</div>
              </div>
            </div>
            <StaticFooter />
          </ShellFrame>
        ))}
      </div>

      {/* Individual nav item states */}
      <div className="mb-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Nav Item States</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <div className="flex gap-3 flex-wrap">
          {[
            { label: 'Default', cls: 'text-neutral-600', iconCls: '', tag: '' },
            { label: 'Hover', cls: 'text-neutral-300 bg-neutral-800/50', iconCls: '', tag: ':hover' },
            { label: 'Active', cls: 'bg-neutral-800 text-white shadow-inner', iconCls: 'text-green-400', tag: '.active' },
            { label: 'Settings Default', cls: 'text-neutral-600', iconCls: '', tag: '' },
            { label: 'Settings Active', cls: 'bg-neutral-800 text-white', iconCls: 'text-green-400', tag: '.active' },
          ].map(s => (
            <div key={s.label} className="flex flex-col gap-1.5">
              <div className={`flex items-center gap-2 px-3 py-2 rounded-[2px] text-[10px] font-mono uppercase tracking-widest w-[160px] border border-neutral-900 ${s.cls}`}>
                {s.label.includes('Settings') ? <Icon.Sliders size={12} className={s.iconCls} /> : <Icon.Shield size={12} className={s.iconCls} />}
                {s.label.includes('Settings') ? 'SETTINGS' : 'CATEGORY 01'}
              </div>
              <div className="text-[8px] font-mono text-neutral-700 pl-1">{s.label}{s.tag ? ` ${s.tag}` : ''}</div>
            </div>
          ))}
        </div>
      </div>

      {/* Sidebar anatomy */}
      <div className="mb-4">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Sidebar Anatomy</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <div className="grid grid-cols-2 gap-8 max-w-[600px]">
          {[
            { rule: 'Width', value: '180px fixed — never flex-grow' },
            { rule: 'Item padding', value: '8px 12px (py-2 px-3)' },
            { rule: 'Item radius', value: '--r-1 (2px)' },
            { rule: 'Font', value: 'JetBrains Mono 10px / tracking-widest / uppercase' },
            { rule: 'Active bg', value: 'neutral-800 + inner shadow' },
            { rule: 'Active icon', value: 'color: --ac (#4ADE80)' },
            { rule: 'Settings separator', value: 'border-t neutral-800/80, p-2 bottom' },
            { rule: 'Tab change', value: 'Resets selectedModule to null' },
          ].map(r => (
            <div key={r.rule} className="flex justify-between text-[9px] font-mono border-b border-neutral-900 pb-1.5">
              <span className="text-neutral-600">{r.rule}</span>
              <span className="text-neutral-400 text-right max-w-[260px]">{r.value}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// MODULE STATES PAGE
// ─────────────────────────────────────────────
function ModuleStatesPage() {
  const moduleStates = [
    { id: 's1', name: 'MODULE A', desc: 'Enabled state. Green status dot with glow. Border tinted green-900.', enabled: true,  disabled: false },
    { id: 's2', name: 'MODULE B', desc: 'Default inactive state. Neutral border and status dot. No glow.', enabled: false, disabled: false },
    { id: 's3', name: 'MODULE C', desc: 'Hover state. Border elevates to neutral-600. Cursor pointer.', enabled: false, disabled: false, hover: true },
    { id: 's4', name: 'MODULE D', desc: 'Disabled / locked state. 50% opacity, no pointer events, gear greyed.', enabled: false, disabled: true },
    { id: 's5', name: 'MODULE E', desc: 'Enabled + hover. Green accent preserved, border reinforced.', enabled: true,  disabled: false, hover: true },
  ]

  return (
    <div className="h-full overflow-y-auto bg-neutral-950 p-8">
      <div className="mb-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-green-500 uppercase mb-2">State documentation</div>
        <h1 className="text-[20px] font-bold tracking-[0.2em] font-mono text-white mb-1">MODULE STATES</h1>
        <p className="text-[10px] text-neutral-500 font-mono">Module card variants, detail view anatomy, and all control states in context.</p>
      </div>

      {/* Card states */}
      <div className="mb-4">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Module Card States</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
      </div>
      <div className="grid grid-cols-3 gap-3 mb-10">
        {moduleStates.map(m => (
          <div key={m.id} className="flex flex-col gap-1.5">
            <div className={`p-3 border rounded-[2px] bg-neutral-950/80 flex flex-col gap-2 transition-all
              ${m.disabled ? 'opacity-50' : ''}
              ${m.enabled ? 'border-green-900/50' : m.hover ? 'border-neutral-600' : 'border-neutral-800'}`}>
              <div className="flex items-start justify-between">
                <div className="flex items-center gap-2">
                  <div className={`w-1.5 h-1.5 rounded-full ${m.enabled ? 'bg-green-500 shadow-[0_0_8px_rgba(74,222,128,0.6)]' : 'bg-neutral-700'}`} />
                  <span className="text-[11px] font-bold text-neutral-100 font-mono tracking-wide">{m.name}</span>
                </div>
                <div className="flex items-center gap-1.5">
                  <div className={`p-1.5 rounded-[2px] border ${m.disabled ? 'opacity-50 border-transparent text-neutral-600' : 'border-transparent text-neutral-400'}`}>
                    <Settings size={14} />
                  </div>
                  <Toggle enabled={m.enabled} />
                </div>
              </div>
              <div className="text-[10px] text-neutral-400 leading-relaxed font-mono">{m.desc}</div>
            </div>
            <div className="text-[8px] font-mono text-neutral-700 pl-1">
              {m.disabled ? 'disabled={true}' : m.hover && m.enabled ? 'enabled + :hover' : m.hover ? ':hover' : m.enabled ? 'enabled={true}' : 'default'}
            </div>
          </div>
        ))}
      </div>

      {/* Detail view snapshot inside a shell */}
      <div className="mb-4">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Module Detail View</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
      </div>
      <ShellFrame label="Detail view — MODULE C selected">
        <StaticHeader />
        <div className="flex" style={{ height: 340 }}>
          <StaticSidebar activeTab="CATEGORY 01" />
          <div className="flex-1 p-4 overflow-hidden">
            {/* Breadcrumb */}
            <div className="flex items-center gap-2 mb-4 text-[9px] font-mono tracking-widest">
              <span className="text-neutral-600">DIRECTORY</span>
              <ChevronRight size={10} className="text-neutral-700" />
              <span className="text-white font-bold">MODULE C</span>
            </div>
            <div className="flex gap-5 h-full">
              {/* Left panel */}
              <div className="w-[140px] flex flex-col gap-2.5 shrink-0">
                <div className="p-3 bg-neutral-900/50 border border-neutral-800 rounded-[2px]">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-[8px] text-neutral-600 uppercase tracking-widest font-mono">Status</span>
                    <Toggle enabled={true} />
                  </div>
                  <div className="text-[8px] text-neutral-600 font-mono uppercase tracking-widest mb-1">Description</div>
                  <p className="text-[8px] text-neutral-500 leading-relaxed font-mono">Extended feature set with configurable parameters.</p>
                </div>
                <div className="flex items-center gap-1.5 px-2 py-1.5 rounded-[2px] text-[8px] font-mono bg-red-950/30 border border-red-900 text-red-400">
                  <RotateCcw size={10} /> RESET MODULE
                </div>
              </div>
              {/* Right panel */}
              <div className="flex-1 flex flex-col gap-3">
                <div className="text-[8px] text-neutral-700 uppercase tracking-widest font-mono border-b border-neutral-800 pb-1.5">Configuration Parameters</div>
                <div className="grid grid-cols-2 gap-x-5 gap-y-3">
                  <div className="flex flex-col gap-1">
                    <div className="flex justify-between text-[8px] font-mono text-neutral-600 uppercase tracking-widest"><span>INTENSITY</span><span className="text-neutral-300 tabular-nums">75%</span></div>
                    <div className="h-1 bg-neutral-900 border border-neutral-800 rounded-[1px] relative">
                      <div className="absolute top-0 bottom-0 left-0 bg-neutral-300 w-3/4 rounded-[1px]" />
                      <div className="absolute top-1/2 -translate-y-1/2 w-1.5 h-2.5 bg-white" style={{ left: 'calc(75% - 3px)' }} />
                    </div>
                  </div>
                  <div className="flex flex-col gap-1">
                    <div className="flex justify-between text-[8px] font-mono text-neutral-600 uppercase tracking-widest"><span>DISTANCE</span><span className="text-neutral-300 tabular-nums">40%</span></div>
                    <div className="h-1 bg-neutral-900 border border-neutral-800 rounded-[1px] relative">
                      <div className="absolute top-0 bottom-0 left-0 bg-neutral-300 w-2/5 rounded-[1px]" />
                      <div className="absolute top-1/2 -translate-y-1/2 w-1.5 h-2.5 bg-white" style={{ left: 'calc(40% - 3px)' }} />
                    </div>
                  </div>
                  <div className="col-span-2 flex gap-3 p-2 bg-neutral-900/30 border border-neutral-800 rounded-[2px]">
                    <Checkbox label="ENABLE DIAGNOSTICS" checked={true} />
                    <Checkbox label="EXPERIMENTAL" checked={false} />
                  </div>
                  <div className="col-span-2 flex flex-col gap-1.5">
                    <div className="text-[8px] font-mono text-neutral-700 uppercase tracking-widest">Keybinds</div>
                    <div className="flex items-center justify-between text-[8px] font-mono">
                      <span className="text-neutral-500 uppercase tracking-widest">PRIMARY ACTION</span>
                      <div className="px-2 py-0.5 bg-neutral-900 border border-neutral-700 rounded-[2px] text-neutral-300 min-w-[48px] text-center">MOUSE 4</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <StaticFooter />
      </ShellFrame>

      {/* State token table */}
      <div className="mt-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Card State Tokens</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <div className="border border-neutral-800 rounded-[2px] overflow-hidden max-w-[640px]">
          <div className="grid grid-cols-[120px_1fr_1fr] text-[8px] font-mono tracking-widest text-neutral-700 uppercase px-3 py-2 bg-neutral-900/40 border-b border-neutral-800">
            <span>State</span><span>Border</span><span>Status dot</span>
          </div>
          {[
            ['Enabled',   'green-900/50',  'bg-green-500 + glow'],
            ['Default',   'neutral-800',   'bg-neutral-700'],
            ['Hover',     'neutral-600',   'bg-neutral-700'],
            ['Disabled',  'neutral-800',   'bg-neutral-700'],
          ].map(([s, b, d], i) => (
            <div key={s} className={`grid grid-cols-[120px_1fr_1fr] px-3 py-2 text-[9px] font-mono ${i % 2 === 0 ? 'bg-neutral-950' : 'bg-neutral-900/20'}`}>
              <span className="text-neutral-400">{s}</span>
              <span className="text-neutral-600">{b}</span>
              <span className="text-neutral-600">{d}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// SETTINGS STATES PAGE
// ─────────────────────────────────────────────
function SettingsStatesPage() {
  return (
    <div className="h-full overflow-y-auto bg-neutral-950 p-8">
      <div className="mb-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-green-500 uppercase mb-2">State documentation</div>
        <h1 className="text-[20px] font-bold tracking-[0.2em] font-mono text-white mb-1">SETTINGS STATES</h1>
        <p className="text-[10px] text-neutral-500 font-mono">Settings view in context, all control states, and section anatomy.</p>
      </div>

      {/* Full settings view in shell */}
      <div className="mb-10">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Settings View — Full Context</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <ShellFrame label="Settings tab active">
          <StaticHeader />
          <div className="flex" style={{ height: 360 }}>
            <StaticSidebar activeTab="SETTINGS" />
            <div className="flex-1 p-5 overflow-y-auto">
              <div className="mb-4">
                <h2 className="text-[14px] font-bold text-white tracking-[0.2em] font-mono">GLOBAL SETTINGS</h2>
                <p className="text-[9px] text-neutral-600 font-mono mt-0.5 uppercase tracking-widest">Client-wide configuration and environmental overrides.</p>
              </div>
              <div className="max-w-lg flex flex-col gap-4">
                {/* Appearance */}
                <div>
                  <div className="text-[8px] text-neutral-700 uppercase tracking-widest font-mono border-b border-neutral-800 pb-1.5 mb-3">Appearance</div>
                  <div className="flex flex-col gap-3">
                    <Slider label="GUI SCALE" value={80} />
                    <Dropdown label="THEME ACCENT" value="DEFAULT (GREEN)" />
                    <Checkbox label="ENABLE BLUR EFFECTS" checked={true} />
                    <Checkbox label="SCANLINE OVERLAY" checked={false} />
                  </div>
                </div>
                {/* Performance */}
                <div>
                  <div className="text-[8px] text-neutral-700 uppercase tracking-widest font-mono border-b border-neutral-800 pb-1.5 mb-3">Performance</div>
                  <div className="flex flex-col gap-3">
                    <Slider label="TICK RATE LIMIT" value={100} />
                    <Dropdown label="RENDER BACKEND" value="AUTO-DETECT" />
                  </div>
                </div>
                {/* System */}
                <div>
                  <div className="text-[8px] text-neutral-700 uppercase tracking-widest font-mono border-b border-neutral-800 pb-1.5 mb-3">System</div>
                  <div className="flex items-center gap-2.5 mb-3">
                    <Toggle enabled={true} />
                    <span className="text-[9px] text-neutral-500 font-mono">Anonymous telemetry enabled.</span>
                  </div>
                  <div className="flex gap-2">
                    <Btn variant="default">EXPORT CONFIGURATION</Btn>
                    <Btn variant="ghost">IMPORT</Btn>
                    <Btn variant="danger">FACTORY RESET</Btn>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <StaticFooter />
        </ShellFrame>
      </div>

      {/* Control states side by side */}
      <div className="mb-8">
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Control States — All Variants</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <div className="grid grid-cols-3 gap-6">

          {/* Slider states */}
          <div className="flex flex-col gap-2">
            <div className="text-[9px] font-mono text-neutral-600 uppercase tracking-widest mb-1">Slider</div>
            <Slider label="HIGH VALUE" value={90} />
            <Slider label="MID VALUE" value={50} />
            <Slider label="LOW VALUE" value={10} />
          </div>

          {/* Dropdown states */}
          <div className="flex flex-col gap-2">
            <div className="text-[9px] font-mono text-neutral-600 uppercase tracking-widest mb-1">Dropdown</div>
            <Dropdown label="DEFAULT STATE" value="PERFORMANCE" />
            <div className="flex flex-col gap-1.5">
              <div className="text-[10px] text-neutral-400 uppercase tracking-widest font-mono">HOVER STATE</div>
              <div className="flex justify-between items-center px-2 py-1.5 bg-neutral-900 border border-neutral-500 rounded-[2px] text-[11px] text-neutral-200 cursor-pointer">
                <span className="font-mono">PERFORMANCE</span>
                <ChevronRight size={12} className="rotate-90 opacity-50" />
              </div>
            </div>
            <div className="flex flex-col gap-1.5">
              <div className="text-[10px] text-neutral-400 uppercase tracking-widest font-mono">OPEN STATE</div>
              <div className="relative">
                <div className="flex justify-between items-center px-2 py-1.5 bg-neutral-900 border border-neutral-500 rounded-t-[2px] text-[11px] text-neutral-200 border-b-0">
                  <span className="font-mono">PERFORMANCE</span>
                  <ChevronRight size={12} className="-rotate-90 opacity-70" />
                </div>
                <div className="border border-neutral-700 border-t-0 rounded-b-[2px] bg-neutral-900">
                  {['PERFORMANCE', 'QUALITY', 'BALANCED'].map((o, i) => (
                    <div key={o} className={`px-2 py-1.5 text-[10px] font-mono ${i === 0 ? 'text-white bg-neutral-800' : 'text-neutral-500 hover:bg-neutral-800'}`}>{o}</div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* Toggle + Checkbox states */}
          <div className="flex flex-col gap-3">
            <div className="text-[9px] font-mono text-neutral-600 uppercase tracking-widest mb-1">Toggle / Checkbox</div>
            {[
              { label: 'Toggle ON',  el: <Toggle enabled={true} /> },
              { label: 'Toggle OFF', el: <Toggle enabled={false} /> },
            ].map(({ label, el }) => (
              <div key={label} className="flex items-center gap-3">
                {el}
                <span className="text-[9px] font-mono text-neutral-600">{label}</span>
              </div>
            ))}
            <div className="mt-1 flex flex-col gap-2">
              <Checkbox label="CHECKED" checked={true} />
              <Checkbox label="UNCHECKED" checked={false} />
            </div>
          </div>
        </div>
      </div>

      {/* Section anatomy */}
      <div>
        <div className="text-[9px] font-mono tracking-[0.2em] text-neutral-600 uppercase mb-4 flex items-center gap-3">
          <span>Section Anatomy</span><div className="flex-1 h-px bg-neutral-800" />
        </div>
        <div className="grid grid-cols-2 gap-5 max-w-[600px]">
          {[
            { rule: 'Section label', value: '8px mono / tracking-widest / uppercase / text-neutral-700' },
            { rule: 'Section divider', value: 'border-b border-neutral-800, pb-1.5–pb-2, mb-3' },
            { rule: 'Control gap', value: 'gap-3 (12px) between controls within a section' },
            { rule: 'Section gap', value: 'gap-4–gap-5 (16–20px) between sections' },
            { rule: 'Max width', value: 'max-w-2xl (672px) for readable form layouts' },
            { rule: 'Button row', value: 'flex gap-2, left-aligned, no full-width except mobile' },
          ].map(r => (
            <div key={r.rule} className="flex justify-between text-[9px] font-mono border-b border-neutral-900 pb-1.5">
              <span className="text-neutral-600">{r.rule}</span>
              <span className="text-neutral-400 text-right max-w-[300px]">{r.value}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────
// ROOT — DESIGN SYSTEM PAGE SWITCHER
// ─────────────────────────────────────────────
export default function App() {
  const [dsPage, setDsPage] = useState('PROTOTYPE')

  const pages = [
    'DESIGN SYSTEM',
    'COMPONENTS',
    'MAIN MENU',
    'NAVIGATION STATES',
    'MODULE STATES',
    'SETTINGS STATES',
    'PROTOTYPE',
  ]

  const renderContent = () => {
    switch (dsPage) {
      case 'DESIGN SYSTEM':     return <DesignSystemPage />
      case 'COMPONENTS':        return <ComponentsPage />
      case 'MAIN MENU':         return <GameClientShell initialTab={CATEGORIES[0]} />
      case 'NAVIGATION STATES': return <NavigationStatesPage />
      case 'MODULE STATES':     return <ModuleStatesPage />
      case 'SETTINGS STATES':   return <SettingsStatesPage />
      case 'PROTOTYPE':         return <GameClientShell />
      default:                  return null
    }
  }

  return (
    <div className="w-screen h-screen flex bg-black overflow-hidden font-mono">
      {/* DS nav sidebar */}
      <div className="w-44 bg-neutral-950 border-r border-neutral-900 flex flex-col z-50 shrink-0">
        <div className="p-3 border-b border-neutral-900">
          <div className="text-[9px] font-bold text-neutral-500 font-mono tracking-[0.15em] uppercase">DS PAGES</div>
        </div>
        <div className="flex-1 py-2 flex flex-col gap-0.5 px-2 overflow-y-auto">
          {pages.map(p => (
            <button
              key={p}
              onClick={() => setDsPage(p)}
              className={`w-full text-left px-2 py-1.5 rounded-[2px] text-[9px] font-mono uppercase tracking-widest transition-all cursor-pointer ${dsPage === p ? 'bg-neutral-800 text-white' : 'text-neutral-600 hover:bg-neutral-900 hover:text-neutral-300'}`}
            >
              {p}
            </button>
          ))}
        </div>
        <div className="p-3 border-t border-neutral-900 text-[8px] font-mono text-neutral-800 tabular-nums">v1.0.0-rc</div>
      </div>

      {/* Content area */}
      <div className="flex-1 relative overflow-hidden">
        {renderContent()}
      </div>
    </div>
  )
}
