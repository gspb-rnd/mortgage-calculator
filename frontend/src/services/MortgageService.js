import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const calculateMortgageOptions = async (mortgageInput) => {
  try {
    console.log('Sending mortgage data to backend:', mortgageInput);
    const response = await axios.post(`${API_URL}/mortgage/calculate`, mortgageInput);
    return response.data;
  } catch (error) {
    console.error('Error in API call:', error);
    throw error;
  }
};

const MortgageService = {
  calculateMortgageOptions
};

export default MortgageService;
