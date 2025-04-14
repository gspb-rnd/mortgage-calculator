import React from 'react';
import MortgageCalculator from './components/MortgageCalculator';

function App() {
  return (
    <div className="max-w-7xl mx-auto p-5 text-center">
      <header className="mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Mortgage Calculator</h1>
      </header>
      <main className="bg-white rounded-lg shadow-md p-6">
        <MortgageCalculator />
      </main>
    </div>
  );
}

export default App;
