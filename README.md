## Unidad III

Arquitectura

Sensor PPG (Wear OS)
│ Health Services API
▼
PassiveListenerService (wear)
│ MessageClient (BLE)
▼
WearListenerService (app)
│ SmartHealthRepository
▼
StateFlow<Int> (fcActual) ──────────────────────────────────┐
│                                                        │
▼                                                        ▼
DashboardViewModel (app)                            TvViewModel (tv)
│ collectAsState()                                │ collectAsState()
▼                                                        ▼
DashboardScreen (Compose)                     TvCatalogScreen (Compose TV)
└── CastButton ──► Chromecast (Remote Playback)

Room DB (LecturaFC) ◄── Repository ──► Flow<List<LecturaFC>>
│
┌─────────────────────┴──────────┐
▼                                 ▼
HistorialScreen (app)          TvCatalogScreen (tv)