import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import UploadDocument from './pages/UploadDocument';
import VerifyDocument from './pages/VerifyDocument';
import ProtectedRoute from './components/common/ProtectedRoute';
import './styles/App.css';

// Placeholder Dashboard for routing test
const Dashboard = () => {
    return (
        <div className="dashboard-container">
            <h2>Welcome to Authenticity Validator</h2>
            <p>You are successfully logged in!</p>
            <div style={{display: 'flex', gap: '1rem', marginTop: '1rem'}}>
                <Link to="/upload" className="primary-btn" style={{textDecoration: 'none', display: 'inline-block'}}>Go to Upload</Link>
                <Link to="/verify" className="primary-btn" style={{textDecoration: 'none', display: 'inline-block', backgroundColor: '#10b981'}}>Verify Document</Link>
            </div>
        </div>
    );
};

function App() {
  return (
    <AuthProvider>
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    
                    <Route path="/dashboard" element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    } />

                    <Route path="/upload" element={
                        <ProtectedRoute>
                            <UploadDocument />
                        </ProtectedRoute>
                    } />

                    <Route path="/verify" element={
                        <ProtectedRoute>
                            <VerifyDocument />
                        </ProtectedRoute>
                    } />
                    
                    {/* Admin only route example */}
                    <Route path="/admin" element={
                        <ProtectedRoute allowedRoles={['Admin']}>
                            <div className="dashboard-container"><h2>Admin Panel</h2></div>
                        </ProtectedRoute>
                    } />
                </Routes>
            </div>
        </Router>
    </AuthProvider>
  );
}

export default App;
