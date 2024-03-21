import React, { useState, useEffect } from 'react';
import './App.css';

const cities = [
  { name: 'Rochester, NY', coordinates: '43.165556,-77.611389' },
  { name: 'Brockport, NY', coordinates: '43.214167,-77.939444' },
  { name: 'Wilmington, DE', coordinates: '39.745833,-75.546667' },
  { name: 'Los Angeles, CA', coordinates: '34.02,-118.7421' },
  { name: 'San Diego, CA', coordinates: '32.715,-117.1625' },
];

const App = () => {
  const [selectedCity, setSelectedCity] = useState(cities[0]);
  const [forecastDays, setForecastDays] = useState(1);
  const [temperature1, setTemperature1] = useState(null);
  const [temperature2, setTemperature2] = useState(null);
  const [temperature3, setTemperature3] = useState(null);
  const [temperature4, setTemperature4] = useState(null);
  const [temperature5, setTemperature5] = useState(null);
  const [temperature6, setTemperature6] = useState(null);
  const [temperature7, setTemperature7] = useState(null);
  const [day2, setDay2] = useState(null);
  const [day3, setDay3] = useState(null);
  const [day4, setDay4] = useState(null);
  const [day5, setDay5] = useState(null);
  const [day6, setDay6] = useState(null);
  const [day7, setDay7] = useState(null);


  useEffect(() => {
    // Fetch current temperature when component mounts
    fetchGrid(selectedCity.coordinates);
  }, [selectedCity]);

  const fetchGrid = async (coordinates) => {
    try {
      // Make fetch request to the weather API
      const response = await fetch(
        `https://api.weather.gov/points/${coordinates}/`
      );

      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }

      const data = await response.json();

      var x = data.properties.gridX;
      var y = data.properties.gridY;
      var office = data.properties.gridId;
      console.log(x, y, office);

      try {
        const response2 = await fetch(
          `https://api.weather.gov/gridpoints/${office}/${x},${y}/forecast`
        );

        const forecast = await response2.json();
        const temp1 = forecast.properties.periods[0].temperature;
        const temp2 = forecast.properties.periods[2].temperature;
        const temp3 = forecast.properties.periods[4].temperature;
        const temp4 = forecast.properties.periods[6].temperature;
        const temp5 = forecast.properties.periods[8].temperature;
        const temp6 = forecast.properties.periods[10].temperature;
        const temp7 = forecast.properties.periods[12].temperature;

        setTemperature1(temp1);
        setTemperature2(temp2);
        setTemperature3(temp3);
        setTemperature4(temp4);
        setTemperature5(temp5);
        setTemperature6(temp6);
        setTemperature7(temp7);

        const day_2 = forecast.properties.periods[2].name;
        const day_3 = forecast.properties.periods[4].name;
        const day_4 = forecast.properties.periods[6].name;
        const day_5 = forecast.properties.periods[8].name;
        const day_6 = forecast.properties.periods[10].name;
        const day_7 = forecast.properties.periods[12].name;

        setDay2(day_2);
        setDay3(day_3);
        setDay4(day_4);
        setDay5(day_5);
        setDay6(day_6);
        setDay7(day_7);


      } catch (error) {
        console.error('Error fetching grid points:', error);
      }

      // Update the state with the temperature
    } catch (error) {
      console.error('Error fetching grid points:', error);
    }
  };

  const handleCityChange = (event) => {
    const selectedCity = cities.find((city) => city.name === event.target.value);
    setSelectedCity(selectedCity);
  };

  const handleDaysChange = (event) => {
    setForecastDays(event.target.value);
  };
  const currentDate = new Date();
  const threeHoursAgo = new Date(currentDate.getTime() - 3 * 60 * 60 * 1000);

  return (
    <div className='content'>
      <div className='dateTime'>
        {(selectedCity.name === 'Los Angeles, CA' || selectedCity.name === 'San Diego, CA') ? (
          <p>
            {threeHoursAgo.toLocaleString()}
          </p>
        ) : (
          <p>
            {new Date().toLocaleString()}
          </p>
        )}
      </div>
      
      <h1>Weather Forecast</h1>

      <form
        
      >
        <label>
          Select a City:
          <select value={selectedCity.name} onChange={handleCityChange}>
            {cities.map((city) => (
              <option key={city.name} value={city.name}>
                {city.name}
              </option>
            ))}
          </select>
        </label>

        <label>
          Select How Many Days:
          <select value={forecastDays} onChange={handleDaysChange}>
            <option value={1}>1 day</option>
            <option value={2}>2 days</option>
            <option value={3}>3 days</option>
            <option value={4}>4 days</option>
            <option value={5}>5 days</option>
            <option value={6}>6 days</option>
            <option value={7}>7 days</option>
          </select>
        </label>


        <div className='today'>
          {temperature1 !== null && (
          <p>
            Current temperature in {selectedCity.name}: {temperature1}°F
          </p>
          )}
        </div>
        <div className='days'>
          
          {forecastDays >= 2 && (
          <p>
            {day2}'s forecast is {temperature2}°F
          </p>
          )}
          {forecastDays >= 3 && (
          <p>
            {day3}'s forecast is {temperature3}°F
          </p>
          )}
          {forecastDays >= 4 && (
          <p>
            {day4}'s forecast is {temperature4}°F
          </p>
          )}
          {forecastDays >= 5 && (
          <p>
            {day5}'s forecast is {temperature5}°F
          </p>
          )}
          {forecastDays >= 6 && (
          <p>
            {day6}'s forecast is {temperature6}°F
          </p>
          )}
          {forecastDays >= 7 && (
          <p>
            {day7}'s forecast is {temperature7}°F
          </p>
          )}
        </div>
      </form>
    </div>

  );
};
export default App;
