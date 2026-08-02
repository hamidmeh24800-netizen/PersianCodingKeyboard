# 🎯 Persian Coding Keyboard

A **real Android Input Method Editor (IME)** built with Kotlin and Android SDK. Replaces your default keyboard with a powerful coding-focused keyboard supporting Persian and English.

## ✨ Features

### Languages
- 🇮🇷 **Persian** — Full support with standard layout
- 🇺🇸 **English** — QWERTY layout with Shift/Caps Lock

### Coding Features
- Dedicated programming symbol row: `{ } [ ] ( ) < > = + - * / \ | : ; " ' \` ~`
- **Tab** key (inserts 4 spaces)
- **Esc**, **Home**, **End** keys
- **Arrow keys** (↑ ↓ ← →)
- **Undo** / **Redo**
- **Python Toolbar** with shortcuts: `def`, `class`, `import`, `if`, `for`, etc.

### Smart Features
- **Auto Pair** — Automatically inserts closing `()`, `[]`, `{}`, `""`, `''`, ` `` `
- **Smart Indentation** — Auto-indents after Python control structures
- **Clipboard Manager** — Stores last 20 copied texts with pinning

### Customization
- **7 Themes**: Dark, Light, Blue, Green, Purple, Cyberpunk, Hacker
- **Key Sound** — Mechanical keyboard feel (Cherry MX Blue style)
- **Vibration** — Adjustable haptic feedback
- **Keyboard Height**, **Key Size**, **Font Size**

### Performance
- ⚡ Fast & memory efficient
- 🔒 No ads, no analytics, no internet permission
- 💯 Offline only

## 📱 Installation

### Method 1: Build from Source
```bash
git clone https://github.com/yourusername/PersianCodingKeyboard.git
cd PersianCodingKeyboard
./gradlew assembleRelease
```

### Method 2: GitHub Actions (No local PC needed)
1. Fork this repository
2. Go to **Actions** tab
3. Click **"Build Release APK"**
4. Download APK from Artifacts

### Method 3: Android Studio
1. Open project in Android Studio
2. Build → Generate Signed Bundle/APK
3. Transfer APK to phone and install

## 🚀 Setup

1. Install the APK
2. Go to **Settings → System → Languages & input → On-screen keyboard**
3. Enable **"Persian Coding Keyboard"**
4. Select it as default keyboard

## 🛠️ Tech Stack

- **Kotlin** — 100% Kotlin codebase
- **Android SDK** — InputMethodService API
- **Material Components** — Modern UI
- **Canvas API** — Custom keyboard rendering
- **SharedPreferences** — Settings storage
- **GitHub Actions** — CI/CD

## 📁 Project Structure

```
PersianCodingKeyboard/
├── app/
│   ├── src/main/
│   │   ├── java/com/persiancodingkeyboard/
│   │   │   ├── service/          # InputMethodService
│   │   │   ├── view/             # Custom KeyboardView
│   │   │   ├── manager/          # Theme, Sound, Vibration, etc.
│   │   │   ├── ui/               # Settings Activity
│   │   │   ├── data/             # Settings Repository
│   │   │   └── util/             # Constants
│   │   └── res/                  # Layouts, Drawables, Values
│   └── build.gradle
├── .github/workflows/             # CI/CD
├── build.gradle
└── README.md
```

## 📝 License

MIT License — see [LICENSE](LICENSE) file.

## 🤝 Contributing

Pull requests are welcome! For major changes, please open an issue first.

---

**Made with ❤️ for Persian developers**
