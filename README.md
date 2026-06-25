# GWeather
A simple weather app with authentication illustrating Android development using MVVM architecture and commonly-used libraries.

---


## Libraries Used

| Library              | Purpose                                                 |
|----------------------|---------------------------------------------------------|
| Hilt                 | Dependency Injection                                    |
| Lifecycle Components | Handling Android-system lifecycle callbacks             |
| Navigation           | Navigating between screens and backstack management     |
| Coroutines           | Performing asynchronous tasks                           |
| Retrofit             | Network api calls                                       |
| Room                 | Local database storage                                  |
| Datastore            | Data storage for small amounts of data                  |
| Material Design      | User interface and user experience                      |
| JUnit                | Unit testing                                            |
| MockK                | Creating mocks for testing                              |

## Architecture
**MVVM Architecture** with three layers:

1. **Model Layer**
    - Repositories for data management

2. **ViewModel Layer**
    - Handles business logic between Model layer and View layer

3. **View Layer**
    - Activity and Fragments observe the ViewModel for ui changes

---

## Setting up the OpenWeather API Key
- Create a dev.properties file in root folder
- Add your api key in the format:
```properties
  OPEN_WEATHER_API_KEY=XXXX
```
