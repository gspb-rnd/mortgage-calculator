import React, { useState } from 'react';
import MortgageService from '../services/MortgageService';
import { Input } from './ui/input';
import { Select } from './ui/select';
import { Button } from './ui/button';
import { Label } from './ui/label';

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
    <div className="max-w-3xl mx-auto">
      <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
        <div className="flex flex-col">
          <Label htmlFor="creditScore">Credit Score (300-850):</Label>
          <Input
            type="number"
            id="creditScore"
            name="creditScore"
            value={formData.creditScore}
            onChange={handleChange}
            min="300"
            max="850"
            required
          />
          {errors.creditScore && <div className="mt-1 text-sm text-red-600 text-left">{errors.creditScore}</div>}
        </div>

        <div className="flex flex-col">
          <Label htmlFor="loanValue">Loan Value ($):</Label>
          <Input
            type="number"
            id="loanValue"
            name="loanValue"
            value={formData.loanValue}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.loanValue && <div className="mt-1 text-sm text-red-600 text-left">{errors.loanValue}</div>}
        </div>

        <div className="flex flex-col">
          <Label htmlFor="state">State:</Label>
          <Select
            id="state"
            name="state"
            value={formData.state}
            onChange={handleChange}
            required
          >
            {stateOptions.map(state => (
              <option key={state} value={state}>{state}</option>
            ))}
          </Select>
        </div>

        <div className="flex flex-col">
          <Label htmlFor="homeType">Home Type:</Label>
          <Select
            id="homeType"
            name="homeType"
            value={formData.homeType}
            onChange={handleChange}
            required
          >
            {homeTypeOptions.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </Select>
        </div>

        <div className="flex flex-col">
          <Label htmlFor="propertyPrice">Property Price ($):</Label>
          <Input
            type="number"
            id="propertyPrice"
            name="propertyPrice"
            value={formData.propertyPrice}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.propertyPrice && <div className="mt-1 text-sm text-red-600 text-left">{errors.propertyPrice}</div>}
        </div>

        <div className="flex flex-col">
          <Label htmlFor="downPayment">Down Payment ($):</Label>
          <Input
            type="number"
            id="downPayment"
            name="downPayment"
            value={formData.downPayment}
            onChange={handleChange}
            min="0"
            required
          />
          {errors.downPayment && <div className="mt-1 text-sm text-red-600 text-left">{errors.downPayment}</div>}
        </div>

        <div className="flex flex-col">
          <Label htmlFor="income">Annual Income ($):</Label>
          <Input
            type="number"
            id="income"
            name="income"
            value={formData.income}
            onChange={handleChange}
            min="1"
            required
          />
          {errors.income && <div className="mt-1 text-sm text-red-600 text-left">{errors.income}</div>}
        </div>

        <div className="flex flex-col">
          <Label htmlFor="points">Points:</Label>
          <Input
            type="number"
            id="points"
            name="points"
            value={formData.points}
            onChange={handleChange}
            step="0.125"
          />
        </div>

        <div className="flex flex-col">
          <Label htmlFor="assetsUnderManagement">Assets Under Management ($):</Label>
          <Input
            type="number"
            id="assetsUnderManagement"
            name="assetsUnderManagement"
            value={formData.assetsUnderManagement}
            onChange={handleChange}
            min="0"
          />
          {errors.assetsUnderManagement && <div className="mt-1 text-sm text-red-600 text-left">{errors.assetsUnderManagement}</div>}
        </div>

        <div className="md:col-span-2">
          <Button 
            type="submit" 
            disabled={loading}
            className="w-full"
            variant="default"
            size="lg"
          >
            {loading ? 'Calculating...' : 'Calculate Mortgage Options'}
          </Button>
        </div>
      </form>

      {apiError && (
        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-md text-red-600 md:col-span-2">
          {apiError}
        </div>
      )}

      {mortgageOptions.length > 0 && (
        <div className="mt-8">
          <h2 className="text-2xl font-bold mb-4 text-gray-800">Mortgage Options</h2>
          <div className="overflow-x-auto">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-gray-100">
                  <th className="px-4 py-3 text-left font-semibold text-gray-700 border-b">Mortgage Type</th>
                  <th className="px-4 py-3 text-left font-semibold text-gray-700 border-b">Rate (%)</th>
                  <th className="px-4 py-3 text-left font-semibold text-gray-700 border-b">Points</th>
                  <th className="px-4 py-3 text-left font-semibold text-gray-700 border-b">APR (%)</th>
                  <th className="px-4 py-3 text-left font-semibold text-gray-700 border-b">Applied Rules</th>
                </tr>
              </thead>
              <tbody>
                {mortgageOptions.map((option, index) => (
                  <tr key={index} className="hover:bg-gray-50">
                    <td className="px-4 py-3 border-b">{option.mortgageType}</td>
                    <td className="px-4 py-3 border-b">{option.rate.toFixed(3)}</td>
                    <td className="px-4 py-3 border-b">{option.points.toFixed(3)}</td>
                    <td className="px-4 py-3 border-b">{option.apr.toFixed(3)}</td>
                    <td className="px-4 py-3 border-b">
                      <ul className="list-disc pl-5">
                        {option.appliedRules.map((rule, ruleIndex) => (
                          <li key={ruleIndex} className="text-sm">{rule}</li>
                        ))}
                      </ul>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};

export default MortgageCalculator;
