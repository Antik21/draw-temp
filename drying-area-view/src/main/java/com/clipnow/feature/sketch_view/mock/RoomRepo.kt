package com.clipnow.feature.sketch_view.mock

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object RoomRepo {

    private const val jsonDataERDTest = """
{
  "rooms": [
            {
              "panoIds": [
                "671b44ad98e2314f028a84ef"
              ],
              "status": "APPROVED",
              "data": {
                "name": "ERD test",
                "version": "0.1",
                "ceilingHeight": null,       
             "corners": [
                                    {
                                        "uid": "77fb6012-c9f2-166b-f283-7cdd7fe1941a",
                                        "origin": {
                                            "x": 660.4,
                                            "y": -354.584
                                        }
                                    },
                                    {
                                        "uid": "fe97ef9d-060c-06ec-e844-cd194387b226",
                                        "origin": {
                                            "x": 660.4,
                                            "y": 134.56714
                                        }
                                    },
                                    {
                                        "uid": "105e4b60-82e9-9247-534c-47158f3dc8c8",
                                        "origin": {
                                            "x": 485.648,
                                            "y": 187.39914
                                        }
                                    },
                                    {
                                        "uid": "f416fc36-1b72-fe88-d583-00220fa8cf8d",
                                        "origin": {
                                            "x": 469.392,
                                            "y": 601.9271
                                        }
                                    },
                                    {
                                        "uid": "23ea731b-3783-1a07-1d2b-3ce42c1f4c75",
                                        "origin": {
                                            "x": -208.61867,
                                            "y": 585.1103
                                        }
                                    },
                                    {
                                        "uid": "4f55dc4d-906a-b0ab-6f62-98305aa3512c",
                                        "origin": {
                                            "x": -186.944,
                                            "y": 299.72
                                        }
                                    },
                                    {
                                        "uid": "04625ac8-21c6-b3f6-4e92-d0cbe4f024e0",
                                        "origin": {
                                            "x": -379.984,
                                            "y": 299.72
                                        }
                                    },
                                    {
                                        "uid": "0f28263e-6d40-f888-7b44-48ae945e64e7",
                                        "origin": {
                                            "x": -367.792,
                                            "y": -354.584
                                        }
                                    }
                                ],
                                "walls": [
                                    {
                                        "uid": "478c46d4-3bf0-d0e4-9a2f-f7d2eba4b049",
                                        "start": "77fb6012-c9f2-166b-f283-7cdd7fe1941a",
                                        "end": "fe97ef9d-060c-06ec-e844-cd194387b226",
                                        "thickness": 10.16,
                                        "windows": null,
                                        "openings": null,
                                        "doors": [
                                            {
                                                "uid": "1c4e0af1-c068-7137-fe0b-6b0a2ab84da2",
                                                "type": "PATH_RIGHT",
                                                "start": 0.34162185,
                                                "width": 161.28,
                                                "isInside": false,
                                                "direction": -1,
                                                "target": "5188426f3260dca2c2781f626d89c19c"
                                            }
                                        ]
                                    },
                                    {
                                        "uid": "4f949aeb-d94b-42ca-eaf6-5ff2b19f4587",
                                        "start": "fe97ef9d-060c-06ec-e844-cd194387b226",
                                        "end": "105e4b60-82e9-9247-534c-47158f3dc8c8",
                                        "thickness": 10.16,
                                        "windows": null,
                                        "openings": null,
                                        "doors": null
                                    },
                                    {
                                        "uid": "3404144f-d31b-9d4f-edb8-4216c53acc23",
                                        "start": "105e4b60-82e9-9247-534c-47158f3dc8c8",
                                        "end": "f416fc36-1b72-fe88-d583-00220fa8cf8d",
                                        "thickness": 10.16,
                                        "windows": [
                                            {
                                                "uid": "6fc81d1c-fd94-6b5c-d3eb-cce9d2f5d8fc",
                                                "start": 0.29743636,
                                                "width": 80.0
                                            }
                                        ],
                                        "openings": null,
                                        "doors": null
                                    },
                                    {
                                        "uid": "559ff4d1-9527-bd80-1e60-a4df48ca2281",
                                        "start": "f416fc36-1b72-fe88-d583-00220fa8cf8d",
                                        "end": "23ea731b-3783-1a07-1d2b-3ce42c1f4c75",
                                        "thickness": 10.16,
                                        "windows": null,
                                        "openings": null,
                                        "doors": null
                                    },
                                    {
                                        "uid": "51379bad-a5e5-71ab-c556-c309554bcb8b",
                                        "start": "23ea731b-3783-1a07-1d2b-3ce42c1f4c75",
                                        "end": "4f55dc4d-906a-b0ab-6f62-98305aa3512c",
                                        "thickness": 10.16,
                                        "windows": null,
                                        "openings": [
                                            {
                                                "uid": "5a1a8e0b-e9be-c50d-42d2-0c7be7586f1b",
                                                "start": 0.42884573,
                                                "width": 80.0
                                            }
                                        ],
                                        "doors": null
                                    },
                                    {
                                        "uid": "20f76832-33c0-0baa-69d0-a0f513d08a3c",
                                        "start": "4f55dc4d-906a-b0ab-6f62-98305aa3512c",
                                        "end": "04625ac8-21c6-b3f6-4e92-d0cbe4f024e0",
                                        "thickness": 10.16,
                                        "windows": null,
                                        "openings": null,
                                        "doors": null
                                    },
                                    {
                                        "uid": "87c74d16-acdf-02da-7254-8cc35efcef20",
                                        "start": "04625ac8-21c6-b3f6-4e92-d0cbe4f024e0",
                                        "end": "0f28263e-6d40-f888-7b44-48ae945e64e7",
                                        "thickness": 10.16,
                                        "windows": [
                                            {
                                                "uid": "1656ca6f-0976-7605-23d0-d76d541d7b10",
                                                "start": 0.20592938,
                                                "width": 80.0
                                            }
                                        ],
                                        "openings": [
                                            {
                                                "uid": "2d92876c-6cdc-5296-16fb-e8731df9a803",
                                                "start": 0.50406903,
                                                "width": 80.0
                                            }
                                        ],
                                        "doors": [
                                            {
                                                "uid": "78296f3a-4b62-4a37-f29b-d15c95d1bfcc",
                                                "type": "PATH_RIGHT",
                                                "start": 0.7461962,
                                                "width": 80.0,
                                                "isInside": false,
                                                "direction": -1,
                                                "target": "5188426f3260dca2c2781f626d89c19c"
                                            }
                                        ]
                                    },
                                    {
                                        "uid": "c58941ce-446c-d6ad-3706-eb5ec6cc0227",
                                        "start": "0f28263e-6d40-f888-7b44-48ae945e64e7",
                                        "end": "77fb6012-c9f2-166b-f283-7cdd7fe1941a",
                                        "thickness": 10.16,
                                        "windows": [
                                            {
                                                "uid": "277dd23d-6c34-3794-519e-6e636632cf7a",
                                                "start": 0.7555926,
                                                "width": 124.704
                                            },
                                            {
                                                "uid": "2500a19e-cd4b-2ad8-f228-cf56f04e2a66",
                                                "start": 0.4196364,
                                                "width": 80.0
                                            },
                                            {
                                                "uid": "0f616776-5091-e391-a757-b29cdb6c12be",
                                                "start": 0.15098216,
                                                "width": 80.0
                                            }
                                        ],
                                        "openings": null,
                                        "doors": null
                                    }
                                ]},
     
               "totalDimensions": {
                                    "floorPerimeter": 3886.1085867334596,
                                    "ceilingPerimeter": 3886.1085867334596,
                                    "floorSquare": 830299.0275203912,
                                    "ceilingSquare": 830299.0275203912,
                                    "wallSquare": 827974.6500714364,
                                    "volumeOfTheRoom": 2.0246011487057215E8
                                }
            }
     ]
}
"""

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

    private const val jsonData2 = """
{
     "rooms": [
            {
              "panoIds": [
                "668cdf5d97260555dd08417f",
                "668cdf5e19472941e899cee9"
              ],
              "status": "APPROVED",
              "data": {
                "name": "room2",
                "version": "0.1",
                "ceilingHeight": null,
                "corners": [
                  {
                    "uid": "f6c61772-024d-ef63-e937-86682e297060",
                    "origin": {
                      "x": 55.88,
                      "y": -268.224
                    }
                  },
                  {
                    "uid": "c6157467-23f7-b75c-a1ac-7725dbc4a861",
                    "origin": {
                      "x": 529.336,
                      "y": -268.224
                    }
                  },
                  {
                    "uid": "12589ef1-685c-4e1f-f5be-037b44976a09",
                    "origin": {
                      "x": 529.336,
                      "y": 284.48
                    }
                  },
                  {
                    "uid": "705f6ac9-c299-a9e2-912b-3f868bfc68d5",
                    "origin": {
                      "x": 55.88,
                      "y": 284.48
                    }
                  }
                ],
                "walls": [
                  {
                    "uid": "9c768be1-ab2e-a63e-1ac9-5f675eebfca7",
                    "start": "f6c61772-024d-ef63-e937-86682e297060",
                    "end": "c6157467-23f7-b75c-a1ac-7725dbc4a861",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "16e8dc11-f181-db53-b9ac-1b26d3e2d376",
                    "start": "c6157467-23f7-b75c-a1ac-7725dbc4a861",
                    "end": "12589ef1-685c-4e1f-f5be-037b44976a09",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "461f68b7-c0a8-8c8f-b610-57827d25b60e",
                    "start": "12589ef1-685c-4e1f-f5be-037b44976a09",
                    "end": "705f6ac9-c299-a9e2-912b-3f868bfc68d5",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "8905f8f6-40f0-2976-fcc6-b6b252639cca",
                    "start": "f6c61772-024d-ef63-e937-86682e297060",
                    "end": "705f6ac9-c299-a9e2-912b-3f868bfc68d5",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  }
                ]
              },
              "totalDimensions": {
                "floorPerimeter": 2011.6800000000003,
                "ceilingPerimeter": 2011.6800000000003,
                "floorSquare": 251358.4650240001,
                "ceilingSquare": 251358.4650240001,
                "wallSquare": 500437.7088,
                "volumeOfTheRoom": 6.1291248111452185E7
              }
            },
            {
              "panoIds": [
                "668cdf5f97260555dd084180"
              ],
              "status": "APPROVED",
              "data": {
                "name": "room1",
                "version": "0.1",
                "ceilingHeight": null,
                "corners": [
                  {
                    "uid": "aadcb549-01db-e527-baaf-93ad3d099fe4",
                    "origin": {
                      "x": -378.968,
                      "y": -442.976
                    }
                  },
                  {
                    "uid": "2b9e95ac-38ed-ec53-f7bb-52bcaa0822e5",
                    "origin": {
                      "x": 657.352,
                      "y": -442.976
                    }
                  },
                  {
                    "uid": "25360cc8-3004-6032-2df1-900b95f340e2",
                    "origin": {
                      "x": 657.352,
                      "y": 719.328
                    }
                  },
                  {
                    "uid": "8592f52d-3e87-3226-2f28-9293541f3e00",
                    "origin": {
                      "x": 3.048,
                      "y": 719.328
                    }
                  },
                  {
                    "uid": "136bf799-9acf-1704-6818-6a80a3da17e0",
                    "origin": {
                      "x": 3.048,
                      "y": 300.736
                    }
                  },
                  {
                    "uid": "8d7325b1-7b66-1c4f-5bb9-4959dfae3a0a",
                    "origin": {
                      "x": -378.968,
                      "y": 300.736
                    }
                  }
                ],
                "walls": [
                  {
                    "uid": "ffb2a572-3f34-6ae7-bbde-086d84e6c97f",
                    "start": "aadcb549-01db-e527-baaf-93ad3d099fe4",
                    "end": "2b9e95ac-38ed-ec53-f7bb-52bcaa0822e5",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "60e8d55c-a3ca-61f4-2e44-039bd93e1d25",
                    "start": "2b9e95ac-38ed-ec53-f7bb-52bcaa0822e5",
                    "end": "25360cc8-3004-6032-2df1-900b95f340e2",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "ad3f8677-3e33-aa0f-c199-609e5e5a55bc",
                    "start": "25360cc8-3004-6032-2df1-900b95f340e2",
                    "end": "8592f52d-3e87-3226-2f28-9293541f3e00",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "b9b14fd0-d0b4-4f81-3434-47e0ab454439",
                    "start": "8592f52d-3e87-3226-2f28-9293541f3e00",
                    "end": "136bf799-9acf-1704-6818-6a80a3da17e0",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "63933635-672d-466e-5d26-61d0585c9ccb",
                    "start": "136bf799-9acf-1704-6818-6a80a3da17e0",
                    "end": "8d7325b1-7b66-1c4f-5bb9-4959dfae3a0a",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  },
                  {
                    "uid": "339f0fc9-cc2a-7ec2-2181-7fcab3251dee",
                    "start": "8d7325b1-7b66-1c4f-5bb9-4959dfae3a0a",
                    "end": "aadcb549-01db-e527-baaf-93ad3d099fe4",
                    "thickness": 10.16,
                    "windows": null,
                    "openings": null,
                    "doors": null
                  }
                ]
              },
              "totalDimensions": {
                "floorPerimeter": 4356.608,
                "ceilingPerimeter": 4356.608,
                "floorSquare": 1022375.2455679998,
                "ceilingSquare": 1022375.2455679998,
                "wallSquare": 1072224.95232,
                "volumeOfTheRoom": 2.4929597987930107E8
              }
            }
          ]
}
"""

    fun getRooms(): List<RoomEntity> {

        val apiRoomsResponse = parseApiRoomsResponseUsingGson(jsonDataERDTest)

        // Step 3: Use RoomMapper to map the JSON rooms data to models
        val roomMapper = RoomMapper()
        val adjustedRooms = adjustCoordinatesForCanvas(apiRoomsResponse.rooms)
        val validatedRooms = adjustedRooms.map { room ->
            val roomData = room.data
            room.copy(
                data = roomData.copy(
                    walls = validateAndFixWallCoordinates(roomData.walls, roomData.corners)
                )
            )
        }

        return roomMapper.mapRooms(validatedRooms)
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