FindMovie Application - Documentation
Welcome to FindMovie, a mobile application designed to help users explore and discover movies effortlessly. This document provides an in-depth explanation of the purpose of the application, how users can interact with its features, the technologies and attributes used in its development, and general guidance on using the application efficiently.

FindMovie is a user-friendly app aimed at movie enthusiasts who want to stay updated on the latest films, search for specific movies, and filter results based on their preferences. Whether you’re looking for a new movie to watch or keeping track of your favorites, FindMovie provides a seamless experience.
Purpose of the Application:
The main goal of FindMovie is to offer users a convenient way to browse and search for movies. The app connects to an external API to retrieve real-time movie data, allowing users to see trending films, search by keywords, and filter movies based on their genre.
Many people struggle to find good movies to watch, and searching for recommendations online can be overwhelming. This app aims to simplify that process by providing a clean and structured interface where users can:

  View a list of popular movies
  Search for movies using a keyword
  Filter movies based on genres
  Like or save favorite movies
  Change the language of the app for a personalized experience

By offering these features, the app ensures that users can quickly and efficiently find movies that match their interests.
How to Use the Application
1. Getting Started
After launching the FindMovie application, users will be greeted by a splash screen, followed by the home screen displaying a list of the most popular movies. The interface is intuitive, making navigation smooth and effortless.
2. Authentication (Login & Registration)
    If you are a new user, you will need to register by providing an email and password.
    If you already have an account, you can log in using your credentials.
    After logging in, you will have access to additional features such as saving favorite movies.
3. Home Screen
    The home screen displays a list of the top five most popular movies at the top.
    Below the popular movies, there is a RecyclerView showing other movies, which can be scrolled through.
    Each movie item includes a poster image, title, and additional details.
4. Searching for a Movie
    There is a search bar at the top where users can type a movie name to find a specific film.
    The app retrieves the search results in real-time and updates the movie list dynamically.
5. Filtering Movies

    Users can filter movies by genre using the filtering options provided.
    When selecting a genre, only movies belonging to that category will be displayed.

6. Liking and Saving Movies

    Users can "like" a movie by clicking the heart icon.
    Liked movies will have a red heart indicator, while unliked ones will have a gray heart.
    All liked movies are stored in Firestore, so they can be accessed even after closing the app.

7. Changing the App Language

    app changes its language according to android's system language

8. Logging Out

    If users wish to log out, they can do so by pressing the logout button in the profile section.
    After logging out, they will be redirected to the Login/Register screen.

Technologies and Attributes Used in the Application

This application was built using various tools, libraries, and best practices to ensure smooth functionality, scalability, and maintainability. Below is a breakdown of the key technologies used:
1. Programming Languages

    Kotlin – The primary language for Android development, ensuring modern, efficient, and clean code.

2. Architecture

    MVVM (Model-View-ViewModel) – Used to separate business logic from UI, making the app easier to maintain and scale.
    Repository Pattern – Helps manage data sources and makes the code more modular and testable.

3. UI Components
    RecyclerView – Used to efficiently display a large list of movies with smooth scrolling.
    ViewPager2 – Allows users to swipe between different pages.
    Fragments – Instead of multiple activities, fragments are used to manage different sections of the app dynamically.

4. Data Storage & API

    Firestore Database – Stores users' liked movies and personal data persistently.
    Retrofit – A powerful library for making API calls, fetching movie data efficiently.
   
6. Dependency Injection

    Hilt (Dagger-Hilt) – Used for dependency injection, making the app more modular and easier to test.

7. State Management & Coroutines

    StateFlow & MutableStateFlow – Used for managing UI state reactively.
    Kotlin Coroutines – Ensures asynchronous tasks, such as API calls, run efficiently without blocking the main thread.

8. Navigation

    Jetpack Navigation Component – Handles in-app navigation smoothly, allowing transitions between fragments.

9. Localization

    values/strings.xml – Stores default English text.
    values-b+ka/strings.xml – Stores Georgian text translations.

Conclusion

FindMovie is designed to offer a simple yet powerful way to discover and keep track of movies. With features like real-time searching, filtering, and liking movies, users can personalize their experience and find the best films effortlessly.

This application follows modern development practices, such as MVVM architecture, dependency injection, and Firestore integration, ensuring that it is not only user-friendly but also robust and maintainable.

By following this documentation, users can fully understand the purpose and functionality of the application while also gaining insights into the technologies used behind the scenes.
