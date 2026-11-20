# navigation-architecture Specification

## Purpose

Defines feature-owned type-safe navigation routes and graph composition across decoupled feature presentation modules.

## Requirements

### Requirement: Feature-owned type-safe routes and graphs
Each feature `presentation` module (`:feature:home:presentation`, `:feature:chart:presentation`, `:feature:analytics:presentation`) SHALL define its own `@Serializable` route objects and expose a `NavGraphBuilder.<feature>Graph(...)` extension function.

#### Scenario: Intra-feature navigation
- **WHEN** a user navigates between screens within the same feature (e.g., `Home` to `HomeAdd` or `HomeEdit`, `Chart` to `ChartDetail`, `Analytics` to `Backup`)
- **THEN** navigation uses that feature's own `@Serializable` routes and `NavController` without importing routes from other features

### Requirement: Umbrella NavHost assembly in composeApp
The `:composeApp` module SHALL assemble all feature navigation graphs in a shared `NavHost` and handle bottom bar / navigation rail tab switching across top-level feature graphs.

#### Scenario: Switching top-level tabs
- **WHEN** a user selects Home, Chart, or Analytics in the bottom bar or navigation rail
- **THEN** `App()` navigates to the corresponding feature graph start destination and highlights the active tab

#### Scenario: Cross-feature navigation decoupling
- **WHEN** a feature needs to trigger navigation outside its own graph
- **THEN** `:composeApp` passes callback lambdas into `<feature>Graph(...)` instead of the feature importing another feature's routes
