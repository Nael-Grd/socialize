import { Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import FeedPage from './pages/FeedPage';

function App() {
  return (
    <Routes>
      {/* Si l'URL est vide (/), on affiche le Login */}
      <Route path="/" element={<LoginPage />} />
      
      {/* Si l'URL est /feed, on affiche le Fil d'actualité */}
      <Route path="/feed" element={<FeedPage />} />
    </Routes>
  );
}

export default App;