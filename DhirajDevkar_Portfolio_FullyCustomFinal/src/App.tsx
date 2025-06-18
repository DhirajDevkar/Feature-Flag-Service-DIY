import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Professional from './pages/Professional';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<h1 style={{ textAlign: 'center', marginTop: '2rem' }}>Welcome to Dhiraj Devkar's Portfolio</h1>} />
        <Route path="/work-experience" element={<Professional />} />
      </Routes>
    </Router>
  );
}

export default App;