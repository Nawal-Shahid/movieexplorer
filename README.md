# Movie Explorer
![Material 3](https://img.shields.io/badge/Material%203-7E57C2?style=for-the-badge&logo=materialdesign&logoColor=white)
![MVVM](https://img.shields.io/badge/MVVM-8E24AA?style=for-the-badge&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/Room-9C27B0?style=for-the-badge&logo=sqlite&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-AB47BC?style=for-the-badge&logo=square&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase%20Auth-BA68C8?style=for-the-badge&logo=firebase&logoColor=white)
![Glide](https://img.shields.io/badge/Glide-CE93D8?style=for-the-badge&logo=android&logoColor=white)

Discover, search, and save your favorite movies with a modern Android app. Built using **Material 3, MVVM, Room, Retrofit, and Firebase Auth**.

---

## Screenshots
<img width="3780" height="1890" alt="Your paragraph text (2)" src="https://github.com/user-attachments/assets/96e19251-2c5f-47ea-af6b-5a00fcaccba7" />


---

## Features

* Popular Movies → Browse trending titles on the Home screen (`HomeFragment`).
* Search → Find movies by title (`SearchFragment`).
* Favorites → Save movies locally with Room (`FavoritesFragment`).
* Movie Details → View posters, ratings, and overviews (`DetailFragment`).
* Profile → Basic app info and logout (`ProfileFragment`).
* Authentication → Firebase Auth (Email/Google Sign-in).
* Offline Persistence → Favorites stored locally; API responses cached.

---

## Tech Stack

* UI: Material 3 (MaterialToolbar, AppBarLayout, BottomNavigationView, SearchBar)
* Architecture: MVVM (View, ViewModel, Repository)
* Database: Room (`MovieDao`, `Movie` entity)
* Networking: Retrofit + OkHttp (Gson converter, logging interceptor)
* Image Loading: Glide
* Auth: Firebase Auth + Google Sign-In
* Navigation: Jetpack Navigation (`nav_graph.xml`)
* Splash: Custom activity-based splash (`SplashActivity`)

---

## Project Structure

```
app/src/main/java/com/example/movieexplorer/
│
├── activity/
│   ├── MainActivity.java        # Nav host + bottom navigation + toolbar
│   ├── SplashActivity.java      # Custom splash, routes to Main/Login
│   ├── LoginActivity.java, SignUpActivity.java
│
├── fragment/
│   ├── HomeFragment.java
│   ├── SearchFragment.java
│   ├── FavoritesFragment.java
│   ├── DetailFragment.java
│   ├── ProfileFragment.java
│
├── adapter/
│   ├── MovieAdapter.java
│   ├── FavoritesAdapter.java
│
├── repository/
│   ├── MovieRepository.java
│
├── viewmodel/
│   ├── MovieViewModel.java
│
├── database/
│   ├── MovieDao.java
│   ├── AppDatabase.java
│   ├── Movie.java
```

**Resources:**

* Layouts: `fragment_home.xml`, `fragment_search.xml`, `fragment_favorites.xml`, `fragment_detail.xml`, `fragment_profile.xml`, etc.
* Navigation: `res/navigation/nav_graph.xml`
* Themes: `res/values/themes.xml` (Material 3, edge-to-edge, dark mode)

---

## Setup & Installation

1. Clone the repo:

   ```bash
   git clone https://github.com/Nawal-Shahid/movieexplorer.git
   cd movie-explorer
   ```
2. Open in **Android Studio Giraffe+** (SDK 36, Java 11 toolchain).
3. Add your **Firebase config**:

   * Place `google-services.json` in `/app/`.
   * Enable Email/Google sign-in in Firebase console.
4. Configure your **Movie API** (e.g., TMDB):

   * Open `ApiClient/ApiService` and set your API base URL + key.
   * Store API keys in `local.properties` or `BuildConfig` (do not hardcode).
5. Sync Gradle and Run ▶

---

## Key Flows

* Splash → `SplashActivity` → routes to `MainActivity` or `LoginActivity`.
* Navigation → `MainActivity` hosts `NavHostFragment` + BottomNavigationView.
* Home → `MovieViewModel.getPopularMovies()` → Retrofit → Room cache.
* Search → Live results via `MovieViewModel.searchMovies(query)`.
* Favorites → Toggle hearts → Persist in Room → Survive restarts.
* Details → Navigate with `movie_id` (Bundle arg).
* Profile → User info + Firebase Auth logout.

---

## UI/UX Details

* Top App Bar → MaterialToolbar via `NavigationUI.setupWithNavController`.
* Bottom Nav → Insets-aware, avoids gesture/nav bar overlap.
* Animations → Heart toggle bounce (`OvershootInterpolator`).
* Profile Screen → Logout button styled with `Widget.Material3.Button.TonalButton`.
* Dark Mode → Configured in `res/values-night/themes.xml`.

---

## Data Persistence

* Entity: `Movie` (`@Entity(tableName = "movies")`, includes `isFavorite`).
* DAO: `MovieDao` with queries (`getFavoriteMovies()`, `isMovieFavorite(id)`, etc.).
* Repository: Preserves `isFavorite` state when caching network results.

---

## Authentication

* Firebase Auth: Email + Google sign-in.
* Ensure SHA keys are added in Firebase console.
* Requires `google-services.json` in `/app/`.

---

## Contributing

* Follow MVVM strictly (View ↔ ViewModel ↔ Repository).
* Use LiveData for reactive UI updates.
* Stick to Material 3 styles for consistency.

---

## License

This project is for educational/demo purposes.

---
