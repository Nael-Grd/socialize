import { Routes, Route } from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import FeedPage from './pages/FeedPage';

function App() {
  return (
    <Routes>
      <Route path="/" element={<AuthPage />} />
      <Route path="/feed" element={<FeedPage />} />
    </Routes>
  );
}

export default App;