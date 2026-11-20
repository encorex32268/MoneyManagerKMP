# Spec Delta

## MODIFIED Requirements

### Requirement: Centralized convention plugins via build-logic
The build system SHALL provide an included `:build-logic` build defining convention plugins for KMP libraries, Compose Multiplatform libraries, Compose feature modules, the Compose umbrella module, the Android application module, Room KMP with KSP, and BuildKonfig configuration.

#### Scenario: Module applies convention plugin
- **WHEN** a `:core:*` or `:feature:*` module applies a convention plugin from `libs.versions.toml`
- **THEN** target platforms, Java 17 / JVM toolchain settings, compiler opt-ins, Room/KSP schema generation (when applied), and shared dependencies are configured without duplicating boilerplate in the module build script
