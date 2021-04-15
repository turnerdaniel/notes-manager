# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added 

- Release docker image to GitHub Container Registry (GHCR)

### Changed

- Updated formatting and name of GitHub Actions workflows to better reflect their purpose.

## [1.1.0] - 2021-04-11

### Added

- New Dockerfile to enable docker support
- Docker-compose with support for volumes which allow persistent storage
- Support for MySQL databases through Spring profiles
- API documentation using Swagger UI hosted on GitHub Pages
- New LICENSE
- New CHANGELOG with retroactive changes

### Changed

- Updated README with details about new changes since `1.0.1`

## [1.0.1] - 2021-03-14

### Security

- Updated Gradle dependency to `6.8.3`
- Updated Spring dependency to `2.4.3`
- Updated Cucumber dependency to `6.10.1`

## [1.0.0] - 2021-03-13

### Added

- Initial release of Note Manager API
- Acceptance tests to determine implementation suitability
- Split  project into 2 sub-projects: `web` and `acceptance-tests` 
- GitHub Actions to perform continuous integration and deployment
- CodeQL code security scanning
- CodeCov and JaCoCo to provide test coverage metrics

[unreleased]: https://github.com/turnerdaniel/notes-manager/compare/1.1.0...HEAD
[1.1.0]: https://github.com/turnerdaniel/notes-manager/compare/1.0.1...1.1.0
[1.0.1]: https://github.com/turnerdaniel/notes-manager/compare/1.0.0...1.0.1
[1.0.0]: https://github.com/turnerdaniel/notes-manager/releases/tag/1.0.0
