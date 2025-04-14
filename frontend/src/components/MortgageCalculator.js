import React, { useState } from 'react';
import MortgageService from '../services/MortgageService';
import './MortgageCalculator.css';

const MortgageCalculator = () => {
  const [formData, setFormData] = useState({
    creditScore: 750,
    loanValue: 400000,
    state: 'CA',
    homeType: 'Single Family',
    propertyPrice: 500000,
    downPayment: 100000,
    income: 120000,
    points: 0,
    assetsUnderManagement: 200000
  });

  const [errors, setErrors] = useState({});
  const [mortgageOptions, setMortgageOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [apiError, setApiError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'state' || name === 'homeType' ? value : Number(value)
    });
  };

  const validateForm = () => {
    const newErrors = {};
    
    if (formData.creditScore < 300 || formData.creditScore > 850) {
      newErrors.creditScore = 'Credit score must be between 300 and 850';
    }
    
    if (formData.loanValue <= 0) {
      newErrors.loanValue = 'Loan value must be positive';
    }
    
    if (formData.propertyPrice <= 0) {
      newErrors.propertyPrice = 'Property price must be positive';
    }
    
    if (formData.downPayment < 0) {
      newErrors.downPayment = 'Down payment must be positive or zero';
    }
    
    if (formData.downPayment > formData.propertyPrice) {
      newErrors.downPayment = 'Down payment cannot exceed property price';
    }
    
    if (formData.income <= 0) {
      newErrors.income = 'Income must be positive';
    }
    
    if (formData.assetsUnderManagement < 0) {
      newErrors.assetsUnderManagement = 'Assets under management must be positive or zero';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setApiError('');
    
    if (validateForm()) {
      setLoading(true);
      try {
        const options = await MortgageService.calculateMortgageOptions(formData);
        setMortgageOptions(options);
      } catch (error) {
        console.error('Error calculating mortgage options:', error);
        if (error.response && error.response.data) {
          const errorMessage = Array.isArray(error.response.data) 
            ? error.response.data.join(', ') 
            : error.response.data;
          setApiError(errorMessage);
        } else {
          setApiError('An error occurred while calculating mortgage options');
        }
        setMortgageOptions([]);
      } finally {
        setLoading(false);
      }
    }
  };

  const stateOptions = [
    'AL', 'AK', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'FL', 'GA',
    'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MD',
    'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ',
    'NM', 'NY', 'NC', 'ND', 'OH', 'OK', 'OR', 'PA', 'RI', 'SC',
    'SD', 'TN', 'TX', 'UT', 'VT', 'VA', 'WA', 'WV', 'WI', 'WY',
    'DC', 'PR', 'VI'
  ];

  const homeTypeOptions = [
    'Single Family',
    'Condo',
    'Townhouse',
    'Multi-Family',
    'Manufactured'
  ];

  return (
    <div className="mortgage-calculator">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="creditScore">Credit Score (300-850):</label>
          <input
            type="number"
            id="creditScore"
            name="creditScore"
            value={formData.creditScore}
            onChange={handleChange}
            min="300"
            max="850"
            required
          />
          {errors.creditScore && <div className="error">{errors.creditScore}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="loanValue">Loan Value ($):</label>
          <input
            type="number"
            id="loanValue"
            name="loanValue"
            value={formData.loanValue}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.loanValue && <div className="error">{errors.loanValue}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="state">State:</label>
          <select
            id="state"
            name="state"
            value={formData.state}
            onChange={handleChange}
            required
          >
            {stateOptions.map(state => (
              <option key={state} value={state}>{state}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="homeType">Home Type:</label>
          <select
            id="homeType"
            name="homeType"
            value={formData.homeType}
            onChange={handleChange}
            required
          >
            {homeTypeOptions.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="propertyPrice">Property Price ($):</label>
          <input
            type="number"
            id="propertyPrice"
            name="propertyPrice"
            value={formData.propertyPrice}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.propertyPrice && <div className="error">{errors.propertyPrice}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="downPayment">Down Payment ($):</label>
          <input
            type="number"
            id="downPayment"
            name="downPayment"
            value={formData.downPayment}
            onChange={handleChange}
            min="0"
            required
          />
          {errors.downPayment && <div className="error">{errors.downPayment}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="income">Annual Income ($):</label>
          <input
            type="number"
            id="income"
            name="income"
            value={formData.income}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.income && <div className="error">{errors.income}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="points">Points:</label>
          <input
            type="number"
            id="points"
            name="points"
            value={formData.points}
            onChange={handleChange}
            step="0.125"
          />
        </div>

        <div className="form-group">
          <label htmlFor="assetsUnderManagement">Assets Under Management ($):</label>
          <input
            type="number"
            id="assetsUnderManagement"
            name="assetsUnderManagement"
            value={formData.assetsUnderManagement}
            onChange={handleChange}
            min="0"
          />
          {errors.assetsUnderManagement && <div className="error">{errors.assetsUnderManagement}</div>}
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Calculating...' : 'Calculate Mortgage Options'}
        </button>
      </form>

      {apiError && <div className="api-error">{apiError}</div>}

      {mortgageOptions.length > 0 && (
        <div className="mortgage-options">
          <h2>Mortgage Options</h2>
          <table>
            <thead>
              <tr>
                <th>Mortgage Type</th>
                <th>Rate (%)</th>
                <th>Points</th>
                <th>APR (%)</th>
                <th>Applied Rules</th>
              </tr>
            </thead>
            <tbody>
              {mortgageOptions.map((option, index) => (
                <tr key={index}>
                  <td>{option.mortgageType}</td>
                  <td>{option.rate.toFixed(3)}</td>
                  <td>{option.points.toFixed(3)}</td>
                  <td>{option.apr.toFixed(3)}</td>
                  <td>
                    <ul>
                      {option.appliedRules.map((rule, ruleIndex) => (
                        <li key={ruleIndex}>{rule}</li>
                      ))}
                    </ul>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default MortgageCalculator;
