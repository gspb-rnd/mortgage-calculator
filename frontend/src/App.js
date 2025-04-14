import React from 'react';
import './App.css';
import MortgageCalculator from './components/MortgageCalculator';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Mortgage Calculator</h1>
      </header>
      <main>
        <MortgageCalculator />
      </main>
    </div>
  );
}

export default App;
