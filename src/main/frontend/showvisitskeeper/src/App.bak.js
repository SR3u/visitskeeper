import logo from './logo.svg';
import './App.css';
/*

{
  "id": "9bc004fb-97ac-4f63-9cb6-ef0910ddcd04",
  "date": "2014-12-30",
  "attendeeIds": [
    "b82f3633-9fbc-4c42-b66b-b99ceb5d5cb9",
    "22560665-c1c0-425a-8941-7e219c9a57ab",
    "bcbc43f2-6f35-4ce7-ac72-816da221dbfe"
  ],
  "artistIds": [
    "bdddeaf5-a619-4496-b539-0c088623d9c5",
    "6f61e7ea-4fda-43b1-9368-5835a381022f",
    "fd26e097-437f-480a-a036-cef48b5325c0",
    "93bb11f8-dc5d-4081-9959-28782993c624",
    "0e2e4219-163c-444e-b57e-209314526a10",
    "ee0e4f75-2ccd-4039-805c-9e4fc8243a13",
    "951771e2-0030-471d-8a7c-417ab59cb42e",
    "64583726-ec4e-4766-8563-2e35a160f1fe",
    "c8033b5b-07fc-4610-b9da-3c553e61d8b4"
  ],
  "conductorId": "e945264e-3b09-455c-9857-e65989692c2a",
  "directorId": "4bdf3dd0-2fd3-4b50-8157-d9b13d84d214",
  "compositionId": "23935b4e-6bc6-4f97-b217-fe475b98b8a9",
  "venueId": "543073fb-3529-47af-9860-eb0b7f755e56",
  "ticketPrice": 600,
  "details": null,
  "perceptionHash": "2014-12-30 23935b4e-6bc6-4f97-b217-fe475b98b8a9 543073fb-3529-47af-9860-eb0b7f755e56",
  "_type": "visit",
  "conductor": {
    "optional": {
      "id": "e945264e-3b09-455c-9857-e65989692c2a",
      "shortName": "гергиев",
      "type": "CONDUCTOR",
      "createdAt": "2026-01-14T21:41:27.618837",
      "_type": "person",
      "displayName": "ГЕРГИЕВ"
    }
  },
  "director": {
    "optional": {
      "id": "4bdf3dd0-2fd3-4b50-8157-d9b13d84d214",
      "shortName": "каран",
      "type": "DIRECTOR",
      "createdAt": "2026-01-14T21:41:29.707078",
      "_type": "person",
      "displayName": "КАРАН"
    }
  },
  "attendees": [
    {
      "id": "22560665-c1c0-425a-8941-7e219c9a57ab",
      "shortName": "остроменская",
      "type": "OTHER",
      "createdAt": "2026-01-14T21:41:29.396811",
      "_type": "person",
      "displayName": "ОСТРОМЕНСКАЯ"
    },
    {
      "id": "b82f3633-9fbc-4c42-b66b-b99ceb5d5cb9",
      "shortName": "юля",
      "type": "FAMILY",
      "createdAt": "2026-01-14T21:41:28.137434",
      "_type": "person",
      "displayName": "ЮЛЯ"
    },
    {
      "id": "bcbc43f2-6f35-4ce7-ac72-816da221dbfe",
      "shortName": "ира",
      "type": "FAMILY",
      "createdAt": "2026-01-14T21:41:27.219391",
      "_type": "person",
      "displayName": "ИРА"
    }
  ],
  "artists": [
    {
      "id": "0e2e4219-163c-444e-b57e-209314526a10",
      "shortName": "абдикеев",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:28.574523",
      "_type": "person",
      "displayName": "АБДИКЕЕВ"
    },
    {
      "id": "64583726-ec4e-4766-8563-2e35a160f1fe",
      "shortName": "агади",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:28.33153",
      "_type": "person",
      "displayName": "АГАДИ"
    },
    {
      "id": "6f61e7ea-4fda-43b1-9368-5835a381022f",
      "shortName": "никитин",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:28.784709",
      "_type": "person",
      "displayName": "НИКИТИН"
    },
    {
      "id": "93bb11f8-dc5d-4081-9959-28782993c624",
      "shortName": "герасимов",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:28.35412",
      "_type": "person",
      "displayName": "ГЕРАСИМОВ"
    },
    {
      "id": "951771e2-0030-471d-8a7c-417ab59cb42e",
      "shortName": "зорин",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:28.479506",
      "_type": "person",
      "displayName": "ЗОРИН"
    },
    {
      "id": "bdddeaf5-a619-4496-b539-0c088623d9c5",
      "shortName": "серов",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:29.588863",
      "_type": "person",
      "displayName": "СЕРОВ"
    },
    {
      "id": "c8033b5b-07fc-4610-b9da-3c553e61d8b4",
      "shortName": "сержан",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:29.113868",
      "_type": "person",
      "displayName": "СЕРЖАН"
    },
    {
      "id": "ee0e4f75-2ccd-4039-805c-9e4fc8243a13",
      "shortName": "алиева",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:29.704764",
      "_type": "person",
      "displayName": "АЛИЕВА"
    },
    {
      "id": "fd26e097-437f-480a-a036-cef48b5325c0",
      "shortName": "воробьев",
      "type": "ACTOR",
      "createdAt": "2026-01-14T21:41:27.302031",
      "_type": "person",
      "displayName": "ВОРОБЬЕВ"
    }
  ],
  "venue": {
    "optional": {
      "id": "543073fb-3529-47af-9860-eb0b7f755e56",
      "shortName": "2мт",
      "createdAt": "2026-01-14T21:41:29.106848",
      "_type": "venue",
      "displayName": "2МТ"
    }
  },
  "shortName": "2014-12-30 23935b4e-6bc6-4f97-b217-fe475b98b8a9",
  "displayName": "2014-12-30 23935B4E-6BC6-4F97-B217-FE475B98B8A9"
}

 */
function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
