/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        primary: { DEFAULT: '#0055aa', 50: '#eff6ff', 100: '#dbeafe', 600: '#004499', 700: '#003377' },
        accent: { DEFAULT: '#ff6600', 600: '#e55a00' }
      },
      fontFamily: { sans: ['Inter', 'system-ui', 'sans-serif'] }
    }
  },
  plugins: []
}
