# Contributing to Raitha Vartha

First off, thank you for considering contributing to Raitha Vartha! It's people like you that make Raitha Vartha such a great tool for empowering farmers.

## Code of Conduct

This project and everyone participating in it is governed by our [Code of Conduct](CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

---

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the [issue list](https://github.com/your-username/Raitha_Vartha/issues) as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps which reproduce the problem**
- **Provide specific examples to demonstrate the steps**
- **Describe the behavior you observed after following the steps**
- **Explain which behavior you expected to see instead and why**
- **Include screenshots and animated GIFs if possible**
- **Include your environment details** (Android version, device model, app version)

### Suggesting Enhancements

Enhancement suggestions are tracked as [GitHub issues](https://github.com/your-username/Raitha_Vartha/issues). When creating an enhancement suggestion, please include:

- **A clear and descriptive title**
- **A step-by-step description of the suggested enhancement**
- **Specific examples to demonstrate the steps**
- **A description of the current behavior and expected behavior**
- **Explain why this enhancement would be useful**

### Pull Requests

- Fill in the required PR template
- Follow the Kotlin coding style guide
- Document new code as per the style guide
- End all files with a newline
- Avoid platform-dependent code
- Add unit tests for new functionality

---

## Development Setup

### Prerequisites

- Android Studio (Ladybug or newer)
- JDK 17 or higher
- Git
- Firebase project with Phone Authentication enabled
- Google Gemini API Key
- Groq API Key

### Local Setup

1. **Fork the repository**

   ```bash
   git clone https://github.com/your-username/Raitha_Vartha.git
   cd Raitha_Vartha
   ```

2. **Create a feature branch**

   ```bash
   git checkout -b feature/YourFeatureName
   ```

3. **Configure API Keys**

   ```bash
   # Create local.properties if it doesn't exist
   echo "GEMINI_API_KEY=your_key_here" >> local.properties
   echo "GROQ_API_KEY=your_key_here" >> local.properties
   ```

4. **Add Firebase Configuration**

   ```bash
   # Copy your google-services.json to app/ directory
   cp ~/Downloads/google-services.json app/
   ```

5. **Open in Android Studio**
   - File → Open → Select Raitha_Vartha folder
   - Wait for Gradle sync

6. **Build the project**
   ```bash
   ./gradlew build
   ```

---

## Coding Guidelines

### Kotlin Style Guide

We follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

- **Naming**: Use camelCase for variables and functions, PascalCase for classes
- **Line Length**: Keep lines to a reasonable length (max 120 characters)
- **Indentation**: Use 4 spaces (never tabs)
- **Comments**: Use meaningful comments; avoid obvious comments
- **Access Modifiers**: Always specify access modifiers explicitly

### Android Best Practices

- Use ViewModels for state management
- Use Repository pattern for data access
- Follow Material Design 3 guidelines
- Implement proper error handling
- Use Kotlin Coroutines for async operations
- Add appropriate null safety checks

### Code Organization

```
com.raithavarta/
├── ai/              # AI service implementations
├── data/            # Data layer (local & remote)
├── model/           # Data models
├── repository/      # Repository implementations
├── ui/              # UI screens and components
├── utils/           # Utility functions
└── viewmodel/       # ViewModels
```

### Commit Message Format

Follow conventional commits format:

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**

- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, missing semicolons, etc.)
- `refactor`: Code refactoring
- `perf`: Performance improvements
- `test`: Adding tests
- `chore`: Build process, dependencies, or tooling changes

**Example:**

```
feat(camera): Add real-time preview in e-Gidha

- Implement camera preview with overlay
- Add frame capture functionality
- Optimize image processing for low-end devices

Closes #123
```

---

## Testing

### Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run tests for specific module
./gradlew app:test
```

### Instrumentation Tests

```bash
# Run instrumentation tests
./gradlew connectedAndroidTest

# Run for specific device
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.notAnnotation=androidx.test.filters.FlakyTest
```

### Test Coverage

```bash
./gradlew testDebugUnitTestCoverage
```

**Coverage Requirements:**

- Aim for at least 70% code coverage
- New features should have >80% coverage
- Critical paths should have >90% coverage

---

## Documentation

### Code Documentation

- Document all public APIs with KDoc comments
- Include usage examples for complex functions
- Document expected exceptions
- Add TODO comments for future improvements

**Example:**

````kotlin
/**
 * Detects crop diseases from an image using Gemini AI.
 *
 * @param imageUri The URI of the crop image to analyze
 * @return A [Result] containing [DiseaseDetectionResponse] or exception
 *
 * @throws IOException if image cannot be read
 * @throws ApiException if Gemini API call fails
 *
 * Example:
 * ```
 * val result = diseaseDetection.detectDisease(imageUri)
 * result.onSuccess { response ->
 *     println("Disease: ${response.disease}")
 * }
 * ```
 */
suspend fun detectDisease(imageUri: Uri): Result<DiseaseDetectionResponse>
````

### README Updates

- Update README.md if adding new features
- Include configuration steps for new dependencies
- Add new features to the roadmap
- Update technology stack if applicable

---

## Review Process

1. **Automated Checks**: All PRs must pass automated checks
   - Code style verification
   - Build verification
   - Unit test verification
   - Lint checks

2. **Code Review**: At least 1 maintainer review required
   - Code quality
   - Test coverage
   - Documentation
   - Performance implications

3. **Approval**: PR must be approved before merging

4. **Merge**: Squash commits and merge to main branch

---

## Release Process

We follow [Semantic Versioning](https://semver.org/):

- **MAJOR**: Incompatible API changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes (backward compatible)

Releases are tagged with `v<major>.<minor>.<patch>`

---

## Additional Notes

### Dependencies

- Check for security vulnerabilities before adding new dependencies
- Prefer well-maintained libraries with good community support
- Keep dependency versions up-to-date
- Document why each dependency is needed

### Performance Considerations

- Profile code before optimization
- Optimize for low-end Android devices (API 26+)
- Monitor memory usage and handle leaks
- Optimize database queries
- Minimize network requests

### Accessibility

- Follow WCAG 2.1 AA standards
- Test with screen readers
- Ensure proper contrast ratios
- Add content descriptions for images
- Support keyboard navigation

---

## Questions?

Feel free to reach out:

- 📧 Email: support@raithavarta.com
- 💬 Discussions: [GitHub Discussions](https://github.com/your-username/Raitha_Vartha/discussions)
- 🐛 Issues: [GitHub Issues](https://github.com/your-username/Raitha_Vartha/issues)

---

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Raitha Vartha! 🌾
