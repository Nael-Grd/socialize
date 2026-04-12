import { Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import FeedPage from './pages/FeedPage';
import ProfilePage from './pages/ProfilePage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<AuthPage />} />
      <Route path="/feed" element={<FeedPage />} />
      <Route path="/profile/:username" element={<ProfilePage />} />
    </Routes>
  );
}

export default App;