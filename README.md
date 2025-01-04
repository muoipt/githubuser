# GitHubUser

A modern Android application built with **Jetpack Compose** to fetch and display GitHub users, along with detailed user information.

---

## 🚀 Overview

This project demonstrates a simple Android app written in **Kotlin** using **Jetpack Compose**. It interacts with the GitHub API to display a list of users, fetch detailed information, and cache user data locally for performance and offline use.

---

## 🌟 Features

- **Fetch GitHub Users with Pagination**  
  Fetch user data in batches of 20 items per page using the [GitHub API](https://api.github.com/users).

- **User Details Page**  
  View detailed information about individual users by querying [https://api.github.com/users/{username}](https://api.github.com/users/{username}).

- **Local Data Caching**  
  User data is stored locally in a database to enable offline access.

- **Unit Tests**  
  Unit tests ensure functionality and robustness.

---

## 📱 User Interface

The app's UI is built entirely with **Jetpack Compose** to provide a modern, responsive, and declarative design.

### 📹 Demo
[![Screen Recording](https://github.com/user-attachments/assets/8e503f19-70e7-4a7b-8266-c1949782ca5c)](https://github.com/user-attachments/assets/8e503f19-70e7-4a7b-8266-c1949782ca5c)

---

## 🛠️ Tech Stack

The app leverages modern Android development tools and frameworks:

- **Architecture**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose, Android paging3
- **Networking**: Retrofit, OkHttp, Moshi
- **Local Storage**: Room Database
- **Dependency Injection**: Hilt
- **Asynchronous Programming**: Coroutines + Kotlin Flow
- **Image Loading**: Coil
- **Code Generation**: KSP
- **Testing**: JUnit, Mockito

---

## 🗂️ Modules Architecture

The app follows a modular architecture for scalability and maintainability.  
<img width="518" alt="Modules Architecture Diagram" src="https://github.com/user-attachments/assets/82cdfa3a-7dec-4df6-b433-d8790f19c883" />

---




