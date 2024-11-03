package com.clipnow.feature.drying_area_view.mock

import android.content.Context
import com.clipnow.feature.drying_area_view.renderer.data.RoomData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object RoomRepo {

    private const val jsonData = """
{
  "rooms": [
            {
              "panoIds": [
                "671b44ad98e2314f028a84ef"
              ],
              "status": "APPROVED",
              "data": {
                "name": "room1",
                "version": "0.1",
                "ceilingHeight": null,
                "corners": [
                  {
                    "uid": "235a9464-ab2e-8fef-3ee4-daf67b871cd7",
                    "origin": {
                      "x": -583.184,
                      "y": -347.472
                    }
                  },
                  {
                    "uid": "a955d9e8-7775-961e-1ea7-c5fc93ad27e6",
                    "origin": {
                      "x": 109.728,
                      "y": -347.472
                    }
                  },
                  {
                    "uid": "101e5a28-8f26-3041-a5a1-34c5e010273e",
                    "origin": {
                      "x": 109.728,
                      "y": 345.44
                    }
                  },
                  {
                    "uid": "db1da3a4-9ffd-da0c-9706-048c0504e1af",
                    "origin": {
                      "x": -583.184,
                      "y": 345.44
                    }
                  }
                ],
                "walls": [
                  {
                    "uid": "4a46097e-1b75-194c-737e-3f1196d88cad",
                    "start": "235a9464-ab2e-8fef-3ee4-daf67b871cd7",
                    "end": "a955d9e8-7775-961e-1ea7-c5fc93ad27e6",
                    "thickness": 10.16,
                    "windows": [
                      {
                        "uid": "6c4d6123-1688-de87-3b26-dcbec1980f7d",
                        "start": 0.320674,
                        "width": 80.0
                      }
                    ],
                    "openings": [
                      {
                        "uid": "82309179-7285-cad5-d1a0-e792429aea5d",
                        "start": 0.7487295,
                        "width": 80.0
                      }
                    ],
                    "doors": null
                  },
                  {
                    "uid": "14ed3044-395b-de7c-8f8d-a358dfde6236",
                    "start": "a955d9e8-7775-961e-1ea7-c5fc93ad27e6",
                    "end": "101e5a28-8f26-3041-a5a1-34c5e010273e",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": [
                      {
                        "uid": "88935f69-6ef8-05b6-32ce-76fd09d45b7c",
                        "type": "PATH_RIGHT",
                        "start": 0.44667143,
                        "width": 80.0,
                        "isInside": true,
                        "direction": 1,
                        "target": "d75bde43aad97de36e40150335ce7e93"
                      }
                    ]
                  },
                  {
                    "uid": "1279ce5a-b56e-2117-918c-6949cd32d152",
                    "start": "101e5a28-8f26-3041-a5a1-34c5e010273e",
                    "end": "db1da3a4-9ffd-da0c-9706-048c0504e1af",
                    "thickness": 10.16,
                    "windows": [
                      {
                        "uid": "d6419f49-22b1-145e-7e89-23fe5efcc440",
                        "start": 0.50535303,
                        "width": 80.0
                      }
                    ],
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "163569f5-d656-a300-a851-fee248ed6be8",
                    "start": "db1da3a4-9ffd-da0c-9706-048c0504e1af",
                    "end": "235a9464-ab2e-8fef-3ee4-daf67b871cd7",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": [
                      {
                        "uid": "8a4d4ac5-c149-3b58-43b4-9f4b83120c6d",
                        "type": "PATH_RIGHT",
                        "start": 0.47612584,
                        "width": 80.0,
                        "isInside": false,
                        "direction": -1,
                        "target": "d75bde43aad97de36e40150335ce7e93"
                      }
                    ]
                  }
                ]
              },
              "totalDimensions": {
                "floorPerimeter": 2731.0080000000007,
                "ceilingPerimeter": 2731.0080000000007,
                "floorSquare": 466150.29350400006,
                "ceilingSquare": 466150.29350400006,
                "wallSquare": 610001.8483200001,
                "volumeOfTheRoom": 1.1366608756801538E8
              }
            }
     ]
}
"""

    fun getRooms(): List<RoomEntity> {

        val apiRoomsResponse = parseApiRoomsResponseUsingGson(jsonData)

        // Step 3: Use RoomMapper to map the JSON rooms data to models
        val roomMapper = RoomMapper()
        val adjustedRooms = adjustCoordinatesForCanvas(apiRoomsResponse.rooms)

        return roomMapper.mapRooms(adjustedRooms)
    }

    // Пример функции для преобразования JSON в объектные модели
    fun parseApiRoomsResponseUsingGson(jsonString: String): ApiRoomsResponse {
        val gson = Gson()

        // Определяем тип для ApiRoomsResponse
        val apiRoomsResponseType = object : TypeToken<ApiRoomsResponse>() {}.type

        // Преобразование JSON строки в объект ApiRoomsResponse
        return gson.fromJson(jsonString, apiRoomsResponseType)
    }
}