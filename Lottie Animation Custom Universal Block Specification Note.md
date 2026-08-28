# Lottie Animation - Custom Universal Block Specification Note
> File Name: Lottie Animation - Custom Universal Block Specification Note.md  
> Author: Myat Gyi  
> System Target: Sketchware Pro Engine & Custom Block Framework  
> Purpose: AI Agent Training, Automated Skill Learning & Knowledge Transfer across LLMs  

---

## 1. System Rules & Architectural Standards

AI Agents / Code Generators must strictly follow these constraints when extending or generating custom blocks for Sketchware Pro:

1. **No Hardcoded View/Activity Dependencies:** 
   - Never use project-specific identifiers or single-activity scopes.
   - Always wrap class operations with Java `instanceof` checks to maintain Universal View compatibility.

2. **Parameter Substitution Rules:**
   - Parameter values inside embedded Java code MUST use positional index format: `%1$s`, `%2$s`, `%3$s`, `%1$d`, `%2$b`, etc.
   - Views declared with `%m.view` or `%m.lottie` map internally to `%1$s` (or their respective positional index).

3. **Language & UX Naturalness:**
   - Block `spec` strings must use natural Burmese syntax suitable for developer readability (e.g., `"View %m.view ၏ Lottie animation ကို စတင်ပြသမည်"`).

---

## 2. Lottie Spec Codes & Imports Directory

### 2.1 View & Input Specifiers
* **Generic View Selector:** `%m.view`
* **Lottie-Specific Selector:** `%m.lottie`
* **String Input (Assets/Path):** `%s`
* **Numeric Input (Speed/Frame):** `%d`
* **Boolean Choice (Looping/State):** `%b`

### 2.2 Standard Library Imports
All Lottie blocks generated must register the necessary imports in the JSON `imports` array:
```json
"imports": [
  "com.airbnb.lottie.LottieAnimationView",
  "com.airbnb.lottie.LottieDrawable",
  "android.animation.Animator"
]

3. Universal Embedded Java Code Rules
​To execute operations safely without requiring add source directly, all code blocks must utilize Dynamic Class Casting:

// Universal Standard Pattern
if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {
    ((com.airbnb.lottie.LottieAnimationView)%1$s).<LOTTIE_METHOD_NAME>();
}

4. Master Custom Blocks JSON Specification
​Below is the verified, error-free JSON payload ready for direct import via Sketchware Pro Block Manager:

[
  {
    "name": "HeaderLottieSection By Myat Gyi",
    "type": "h",
    "color": "#FF4081",
    "palette": "",
    "spec": "=== LOTTIE ANIMATION CONTROLLER SECTION ==="
  },
  {
    "name": "lottieSetAnimationFromAssets",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view တွင် Assets ဖိုင် %s ဖြင့် Lottie animation သတ်မှတ်မည်",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)%1$s).setAnimation(\%2$s);\n}"
  },
  {
    "name": "lottiePlayAnimation",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Lottie animation ကို စတင်ပြသမည်",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)\%1$s).playAnimation();\n}"
  },
  {
    "name": "lottiePauseAnimation",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Lottie animation ကို ခေတ္တရပ်တန့်မည်",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)\%1$s).pauseAnimation();\n}"
  },
  {
    "name": "lottieCancelAnimation",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Lottie animation ကို လုံးဝရပ်တန့်ပြီး အစသို့ ပြန်ပို့မည်",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)\%1$s).cancelAnimation();\n}"
  },
  {
    "name": "lottieIsAnimating",
    "type": "b",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Animation သည် အလုပ်လုပ်နေဆဲဖြစ်သလား",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "(%1$s instanceof com.airbnb.lottie.LottieAnimationView && ((com.airbnb.lottie.LottieAnimationView)\%1$s).isAnimating())"
  },
  {
    "name": "lottieSetRepeatMode",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Lottie animation ကို ထပ်ခါထပ်ခါ ပြသမလား %b",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView",
      "com.airbnb.lottie.LottieDrawable"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)%1$s).setRepeatCount(\%2$b ? com.airbnb.lottie.LottieDrawable.INFINITE : 0);\n}"
  },
  {
    "name": "lottieSetSpeed",
    "type": "",
    "color": "#FF4081",
    "palette": "",
    "spec": "View %m.view ၏ Lottie animation အမြန်နှုန်းကို %d ဖြင့် သတ်မှတ်မည်",
    "imports": [
      "com.airbnb.lottie.LottieAnimationView"
    ],
    "code": "if (%1$s instanceof com.airbnb.lottie.LottieAnimationView) {\n    ((com.airbnb.lottie.LottieAnimationView)%1$s).setSpeed((float)\%2$d);\n}"
  }
]


5. Expansion Guidelines for AI Training
​When an AI Agent is tasked with creating additional Lottie functionality, it must follow these rules:
​Animation Callbacks / Event Listeners: Use inline listener instantiations or custom code snippets without introducing memory leaks.
​Progress & Frame Controls: Map progress (0.0f to 1.0f) using setProgress((float)%2$d) with safety checks.
​Url/Json String Remote Loading: Map setAnimationFromUrl(%2$s) or setAnimationFromJson(%2$s, null) dynamically while retaining the instanceof wrapper pattern.