import axios from 'axios';

const API_URL = 'https://authenticity-validator-academia.onrender.com/api/documents';

// Axios instance to automatically attach the token
const axiosInstance = axios.create({
    baseURL: API_URL
});

axiosInstance.interceptors.request.use((config) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
        config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
});

const uploadDocument = async (file, onUploadProgress) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axiosInstance.post('/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress,
    });
    return response.data;
};

const verifyDocument = async (file, onUploadProgress) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axiosInstance.post('/verify', formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        onUploadProgress,
    });
    return response.data;
};

const documentService = {
    uploadDocument,
    verifyDocument
};

export default documentService;
