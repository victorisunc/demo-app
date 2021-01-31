import React, {useState} from 'react';
import './App.css';
import Ad from "./modules/Ad/Ad";
import {getAds} from "./api/ads";

function App() {

  let [ads, setAds] = useState([]);

  const fetchAds = React.useCallback(() => {
    getAds()
      .then((response) => {
        setAds(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  React.useEffect(() => {
    fetchAds();
    // eslint-disable-next-line
  }, []);

  return (
    <div className="App">
      <h1>Demo App</h1>
      <button type='button' onClick={fetchAds}>Click to Fetch More Ads</button>
      <div className="container">
        {ads.length && ads.map(ad => <Ad {...ad}/> )}
        {!ads.length && <div>No ads available</div>}
      </div>
    </div>
  );
}

export default App;
