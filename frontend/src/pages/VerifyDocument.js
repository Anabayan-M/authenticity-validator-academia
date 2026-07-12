import React, { useState, useRef } from 'react';
import documentService from '../services/documentService';
import '../styles/App.css';

const VerifyDocument = () => {
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');
    const [progress, setProgress] = useState(0);
    const [verifyResult, setVerifyResult] = useState(null);
    const [isDragging, setIsDragging] = useState(false);
    
    const fileInputRef = useRef(null);

    const handleDragEnter = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDragging(true);
    };

    const handleDragLeave = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDragging(false);
    };

    const handleDragOver = (e) => {
        e.preventDefault();
        e.stopPropagation();
    };

    const handleDrop = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDragging(false);
        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            validateAndSetFile(e.dataTransfer.files[0]);
        }
    };

    const handleFileSelect = (e) => {
        if (e.target.files && e.target.files.length > 0) {
            validateAndSetFile(e.target.files[0]);
        }
    };

    const validateAndSetFile = (selectedFile) => {
        setError('');
        setVerifyResult(null);
        setProgress(0);

        if (selectedFile.type !== 'application/pdf') {
            setError('Only PDF files are supported.');
            setFile(null);
            return;
        }

        if (selectedFile.size > 10 * 1024 * 1024) { // 10 MB
            setError('File size must be under 10 MB.');
            setFile(null);
            return;
        }

        setFile(selectedFile);
    };

    const handleVerify = async () => {
        if (!file) return;
        setError('');
        
        try {
            const result = await documentService.verifyDocument(file, (progressEvent) => {
                const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
                setProgress(percentCompleted);
            });
            
            setVerifyResult(result);
            
        } catch (err) {
            setProgress(0);
            if (err.response && err.response.data && err.response.data.message) {
                setError(err.response.data.message);
            } else {
                setError('An error occurred during verification.');
            }
        }
    };

    const resetVerification = () => {
        setFile(null);
        setVerifyResult(null);
        setProgress(0);
        setError('');
    };

    return (
        <div className="upload-container">
            <div className="upload-card">
                <h2>Verify Document Authenticity</h2>
                <p>Upload a PDF to verify if it is an authentic academic document.</p>
                
                {error && <div className="error-alert">{error}</div>}
                
                {!verifyResult ? (
                    <>
                        <div 
                            className={`drop-zone ${isDragging ? 'drag-over' : ''}`}
                            onDragEnter={handleDragEnter}
                            onDragLeave={handleDragLeave}
                            onDragOver={handleDragOver}
                            onDrop={handleDrop}
                            onClick={() => fileInputRef.current.click()}
                        >
                            <input 
                                type="file" 
                                accept="application/pdf"
                                style={{ display: 'none' }} 
                                ref={fileInputRef}
                                onChange={handleFileSelect}
                            />
                            {file ? (
                                <div className="file-info">
                                    <span className="file-name">{file.name}</span>
                                    <span className="file-size">{(file.size / (1024 * 1024)).toFixed(2)} MB</span>
                                </div>
                            ) : (
                                <div className="drop-prompt">
                                    <span>Drag & Drop your PDF here</span>
                                    <span className="drop-or">or</span>
                                    <button type="button" className="secondary-btn">Browse Files</button>
                                </div>
                            )}
                        </div>

                        {progress > 0 && progress < 100 && (
                            <div className="progress-container">
                                <div className="progress-bar" style={{ width: `${progress}%` }}></div>
                                <span className="progress-text">{progress}%</span>
                            </div>
                        )}

                        <button 
                            className="primary-btn upload-btn" 
                            onClick={handleVerify} 
                            disabled={!file || (progress > 0 && progress < 100)}
                        >
                            {progress > 0 && progress < 100 ? 'Verifying...' : 'Verify Document'}
                        </button>
                    </>
                ) : (
                    <div className="success-container">
                        {verifyResult.verified ? (
                            <div className="status-card success">
                                <div className="status-icon">✓</div>
                                <h3>Authentic Document</h3>
                                <p className="status-message">This document matches our records.</p>
                                <div className="metadata-box">
                                    <div className="meta-row">
                                        <span className="meta-label">Document Name:</span>
                                        <span className="meta-value">{verifyResult.documentName}</span>
                                    </div>
                                    <div className="meta-row">
                                        <span className="meta-label">Uploaded By:</span>
                                        <span className="meta-value">{verifyResult.uploadedBy}</span>
                                    </div>
                                    <div className="meta-row">
                                        <span className="meta-label">Upload Date:</span>
                                        <span className="meta-value">{new Date(verifyResult.uploadDate).toLocaleDateString()}</span>
                                    </div>
                                </div>
                            </div>
                        ) : (
                            <div className="status-card error">
                                <div className="status-icon">✗</div>
                                <h3>Document Not Verified</h3>
                                <p className="status-message">This document has been modified or does not exist in our records.</p>
                            </div>
                        )}
                        <button className="secondary-btn mt-4" onClick={resetVerification}>
                            Verify Another Document
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};

export default VerifyDocument;
