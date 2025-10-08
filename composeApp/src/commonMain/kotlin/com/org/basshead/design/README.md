# Basshead Atomic Design System

This design system follows the atomic design methodology popularized by Brad Frost and implemented following Reddit's engineering patterns for scalable, maintainable UI components.

## Architecture Overview

### Design Token Foundation
- **Tokens**: Core design values (colors, typography, spacing, elevation)
- **Theme**: Composition providers and theme management

### Atomic Component Hierarchy

#### 1. Atoms (Foundation Layer)
Basic building blocks that cannot be broken down further:
- Buttons (Primary, Secondary, Text, Icon)
- Typography (Headings, Body, Labels, Captions)
- Form elements (TextField, Checkbox, RadioButton, Switch)
- Icons and visual indicators
- Dividers and spacers
- Progress indicators

#### 2. Molecules (Component Layer)
Groups of atoms functioning together as a unit:
- Search bars (TextField + Icon + Button)
- Navigation items (Icon + Text)
- Form groups (Label + TextField + Error)
- Cards (basic containers)
- Lists items (simple combinations)

#### 3. Organisms (Pattern Layer)
Complex UI components made of molecules and atoms:
- Navigation bars
- Forms
- Data tables
- Feature-specific cards
- Content blocks

#### 4. Templates (Layout Layer)
Page-level layouts defining structure:
- Screen layouts
- Grid systems
- Content organization patterns

#### 5. Pages (Implementation Layer)
Specific instances of templates with real content:
- Actual screens in the app

## Design Principles

1. **Consistency**: Reusable components ensure visual and behavioral consistency
2. **Scalability**: Atomic structure allows for easy maintenance and extension
3. **Performance**: Optimized for Compose with proper state management
4. **Accessibility**: Built-in accessibility support at every level
5. **Testability**: Each component level can be tested independently

## Usage Guidelines

### Component Naming Convention
- Atoms: `BassheadButton`, `BassheadText`, `BassheadIcon`
- Molecules: `BassheadSearchBar`, `BassheadFormField`
- Organisms: `BassheadNavigationBar`, `BassheadFestivalCard`

### Import Structure
```kotlin
// Tokens and theme
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.design.tokens.*

// Atoms
import com.org.basshead.design.atoms.*

// Molecules
import com.org.basshead.design.molecules.*

// Organisms
import com.org.basshead.design.organisms.*
```

### Component Guidelines
- All components must support theming through BassheadTheme
- Use semantic naming for component variants
- Include comprehensive documentation and examples
- Support both light and dark themes
- Include accessibility considerations
