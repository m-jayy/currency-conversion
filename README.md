# Currency Conversion App Following Modern Best Practices

This app allows users to view a given amount in a selected currency, converted into other currencies using real-time exchange rates.

Check out the app's demo video:  
[App Video Link](https://www.youtube.com/shorts/4Ee9whmzfxk)

## Build On

- **Kotlin version:** 2.0
- **Android Studio:** Ladybug
- **Runtime version:** 17.0.11+1-b1312.2 aarch64 (JCEF 122.1.9)
- **VM:** OpenJDK 64-Bit Server VM by JetBrains s.r.o.

## Functional Requirements

- Fetches exchange rates data from the free version of an exchange rates service.
- Perform the conversions on the app side if exchange rates for the selected currency are unavailable through Open Exchange Rates.
- Supports **offline mode**, allowing the app to be used after the data is fetched and persisted locally.
- The app refreshes the required data no more frequently than once every **30 minutes** to minimize bandwidth usage.

## User Interactions

The app's UI is built using **Jetpack Compose** and provides the following features:

- **Currency Amount Input:** The user can enter the amount they want to convert.
- **Currency Selection:** The user can select a currency from a list provided by Open Exchange Rates.
- **Converted Amount Display:** After selection, the app displays the desired amount converted into various currencies.

## Environment Configuration

The app uses the **Base URL** and **OpenExchange App ID** from the `env.secrets.json` file, located in the app directory, to make API requests.

## Testing

- **Unit Tests:** The project contains unit tests that ensure the correct operation of the app.

## Architecture

The app adopts the **MVVM** architecture, following the principles of **The Clean Architecture** and **S.O.L.I.D** to maintain a scalable, maintainable, and testable codebase.

## Libraries and Tools Used

The app uses the following libraries:

- **Kotlin Flow**
- **Coroutines**
- **ViewModel**
- **Jetpack Compose**
- **Dagger Hilt**
- **Retrofit**
- **Room**
- **JUnit**
- **Turbine**
- **Espresso**

**Note:** To run the project, add your Open Exchange `app_id` to `env.secret.json`.
