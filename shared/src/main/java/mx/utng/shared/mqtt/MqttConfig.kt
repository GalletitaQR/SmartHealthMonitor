package mx.utng.shared.mqtt
import mx.utng.shared.BuildConfig

object MqttConfig {
    const val MQTT_BROKER_URL = BuildConfig.MQTT_BROKER_URL
    const val USERNAME = BuildConfig.MQTT_USERNAME
    const val PASSWORD = BuildConfig.MQTT_PASSWORD

    const val TOPIC_FC = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"

    const val QOS = 1
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP = "smarthealthmonitor-app"
    const val CLIENT_TV = "smarthealthmonitor-tv"
}