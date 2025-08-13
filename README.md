# Weatherly - Kotlin Multiplatform Weather App

A modern, cross-platform weather application built with Kotlin Multiplatform and Compose Multiplatform, providing real-time weather information for Android and iOS platforms.

## 🌟 Features

### Core Functionality
- **Real-time Weather Data**: Current weather conditions with temperature, humidity, wind speed, and precipitation
- **7-Day Forecast**: Extended weather forecast with daily temperature and precipitation data
- **Location-based Weather**: Automatic location detection with permission handling
- **Reverse Geocoding**: Display location names based on coordinates
- **Time-based Greetings**: Dynamic greetings based on current time (Good Morning, Good Afternoon, etc.)
- **Weather Icons**: Emoji-based weather condition indicators

### User Experience
- **Onboarding Flow**: First-time user introduction screen
- **Permission Management**: Seamless location permission requests
- **Loading States**: Shimmer effects during data loading
- **Error Handling**: Comprehensive error states and user feedback
- **Responsive UI**: Adaptive design for different screen sizes
- **Navigation Drawer**: Side menu with app information

## 🏗️ Architecture

### Clean Architecture Pattern
The project follows Clean Architecture principles with clear separation of concerns:

```
├── Domain Layer
│   ├── Models (WeatherData, LocationData, PermissionState)
│   ├── Repositories (Interfaces)
│   └── Use Cases (Business Logic)
├── Data Layer
│   ├── Remote (API Services)
│   ├── Local (DataStore Preferences)
│   ├── Repositories (Implementations)
│   └── Mappers (Data Transformation)
└── Presentation Layer
    ├── ViewModels (State Management)
    ├── Screens (UI Components)
    └── Navigation
```

## 🛠️ Technology Stack

### Core Technologies
- **Kotlin Multiplatform**: Shared business logic across platforms
- **Compose Multiplatform**: Declarative UI framework
- **Koin**: Dependency injection framework
- **Ktor**: HTTP client for API communication
- **DataStore**: Local data persistence
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Navigation Compose**: Screen navigation
- **Kotlinx DateTime**: Date and time handling

### Platform-Specific
- **Android**: Google Play Services Location, Activity Result API
- **iOS**: Core Location framework

## 🌐 APIs Used

### Weather Data
- **Open-Meteo API**: Free weather API providing:
  - Current weather conditions
  - 7-day forecast
  - Multiple weather parameters (temperature, humidity, wind, precipitation)
  - Weather codes for condition mapping

### Location Services
- **Nominatim API (OpenStreetMap)**: Reverse geocoding service for:
  - Converting coordinates to human-readable addresses
  - City, town, and village name resolution

## 📱 Platform Support

### Android
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: Latest
- **Permissions**:
  - `ACCESS_FINE_LOCATION`
  - `ACCESS_COARSE_LOCATION`

### iOS
- **Minimum Version**: iOS 14.0+
- **Permissions**: Location When In Use
- **Info.plist**: `NSLocationWhenInUseUsageDescription`

## 🏃‍♂️ Getting Started

### Prerequisites
- Android Studio with Kotlin Multiplatform plugin
- Xcode (for iOS development)
- JDK 11 or higher

### Setup
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run on Android or iOS simulator/device

### Project Structure
```
Weatherly/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/kotlin/    # Shared code
│   │   ├── androidMain/kotlin/   # Android-specific code
│   │   └── iosMain/kotlin/       # iOS-specific code
│   └── build.gradle.kts
├── iosApp/                       # iOS application wrapper
└── settings.gradle.kts
```

## 🔧 Dependency Injection

The app uses Koin for dependency injection with modular organization:

- **LocalModule**: DataStore and local data sources
- **NetworkModule**: HTTP client and API services
- **RepositoryModule**: Repository implementations
- **UseCaseModule**: Business logic use cases
- **ViewModelModule**: Presentation layer ViewModels
- **PlatformModule**: Platform-specific dependencies

## 🎨 UI/UX Features

### Design Elements
- **Material Design 3**: Modern UI components
- **Gradient Backgrounds**: Visually appealing weather displays
- **Weather Icons**: Intuitive emoji-based weather representation
- **Shimmer Loading**: Smooth loading animations
- **Responsive Layout**: Adaptive to different screen sizes

### Weather Condition Mapping
The app maps weather codes to conditions and icons:
- Clear sky ☀️
- Partly cloudy ⛅
- Foggy 🌫️
- Rain 🌧️
- Snow ❄️
- Thunderstorm ⛈️
- And more...

## 📊 State Management

ViewModels manage UI state using:
- **StateFlow**: Reactive state management
- **Coroutines**: Asynchronous operations
- **Result**: Error handling wrapper

## 🔒 Privacy & Permissions

- Location data is only used for weather information
- No personal data is stored or transmitted
- Users can deny location access (app will show permission screen)
- Transparent permission handling with user-friendly messages

## 🚀 Future Enhancements

- Weather alerts and notifications
- Multiple location support
- Weather maps integration
- Historical weather data
- Customizable units (Celsius/Fahrenheit)
- Dark/Light theme toggle
- Weather widgets

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Built with ❤️ using Kotlin Multiplatform**