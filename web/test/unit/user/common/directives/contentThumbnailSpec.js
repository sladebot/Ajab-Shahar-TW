describe("Thumbnail model", function () {
    it("should construct song thumbnail model from song representation", function () {
        var songThumbnail = new AjabShahar.ThumbnailObject(test_songRepresentation[0], "song");

        expect(songThumbnail.type).toBe("song");
        expect(songThumbnail.thumbnailImg).toBe("https://farm8.staticflickr.com/7583/16097980187_72dfa07068_o.png");
        expect(songThumbnail.verbPeople.verb).toBe("SINGS");
        expect(songThumbnail.verbPeople.people).toBe("Mukhtiyar Ali");

        expect(songThumbnail.englishTitle).toBe("Everyday I Ask For Your Well Being");
        expect(songThumbnail.translitTitle).toBe("Nit Khair Manga");

        expect(songThumbnail.contentFormat).toBe("audio");
        expect(songThumbnail.duration).toBe("07:45");

        expect(songThumbnail.secondaryVerbPeople.verb).toBe("POET");
        expect(songThumbnail.secondaryVerbPeople.people).toBe("Badar Muneer");

    });

    it("should construct song thumbnail model from song summary representation", function () {
        var songThumbnail = new AjabShahar.ThumbnailObject(song, "song");

        expect(songThumbnail.type).toBe("song");
        expect(songThumbnail.thumbnailImg).toBe("http://3.bp.blogspot.com/-kwpgiMcXc24/TcOcowo6mTI/AAAAAAAAA9w/uNt6ZsJadDg/s1600/parvathy_baul03.jpg");

        expect(songThumbnail.englishTitle).toBe("For a few days,O Heart");
        expect(songThumbnail.translitTitle).toBe("Kichhu din mone mone");

        expect(songThumbnail.contentFormat).toBe("video");
        expect(songThumbnail.duration).toBe("5:45");

        expect(songThumbnail.secondaryVerbPeople.verb).toBe("POET");
        expect(songThumbnail.secondaryVerbPeople.people).toBe("Roshik");

    });

    it("should construct song thumbnail model from song representation with proper singers information and content format", function () {
        var songThumbnail = new AjabShahar.ThumbnailObject(test_songRepresentation[1], "song");

        expect(songThumbnail.type).toBe("song");
        expect(songThumbnail.thumbnailImg).toBe("https://farm8.staticflickr.com/7496/15609516053_67a9b050e9_o.png");
        expect(songThumbnail.verbPeople.verb).toBe("SING");
        expect(songThumbnail.verbPeople.people).toBe("Parvathy Baul & Badar Muneer");

        expect(songThumbnail.englishTitle).toBe("For a Few Days, O Heart");
        expect(songThumbnail.translitTitle).toBe("Kichhu din mone mone");

        expect(songThumbnail.contentFormat).toBe("video");
        expect(songThumbnail.duration).toBe("05:45");

        expect(songThumbnail.secondaryVerbPeople.verb).toBe("POET");
        expect(songThumbnail.secondaryVerbPeople.people).toBe("Roshik");

    });

});

describe("content thumbnail directive", function () {

    it("should show singers information only when there is only one singer for the song ", function () {
        var songThumbnail = new AjabShahar.ThumbnailObject(test_songRepresentation[1], "song");

        expect(songThumbnail.showPrimaryVerbPeopleAlways()).toBeFalsy();
        expect(songThumbnail.showPrimaryVerbPeopleInDetails()).toBeTruthy();

        var songThumbnail = new AjabShahar.ThumbnailObject(test_songRepresentation[0], "song");

        expect(songThumbnail.showPrimaryVerbPeopleAlways()).toBeTruthy();
        expect(songThumbnail.showPrimaryVerbPeopleInDetails()).toBeFalsy();
    })
});
var test_songRepresentation = [
    {
        "id": 11,
        "isAuthoringComplete": true,
        "showOnLandingPage": true,
        "youtubeVideoId": "",
        "soundCloudTrackId": "https://soundcloud.com/kashanilyas/we-main-chori-chori-tere-nal/sets",
        "thumbnailURL": "https://farm8.staticflickr.com/7583/16097980187_72dfa07068_o.png",
        "duration": "07:45",
        "singers": [
            {
                "id": 25,
                "name": "Mukhtiyar Ali",
                "hindiName": "",
                "primaryOccupation": ""
            }
        ],
        "poets": [
            {
                "id": 26,
                "name": "Badar Muneer",
                "hindiName": "",
                "primaryOccupation": ""
            }
        ],
        "about": "<p>This is about song</p>",
        "songTitle": {
            "id": 50,
            "originalTitle": "नित खैर मंगा",
            "englishTranslation": "Everyday I Ask For Your Well Being",
            "englishTransliteration": "Nit Khair Manga",
            "category": {
                "id": 7,
                "name": "Song Title",
                "categoryType": "Song Title"
            }
        },
        "songCategory": {
            "id": 1,
            "name": "Songs",
            "categoryType": "song"
        },
        "mediaCategory": {
            "id": 4,
            "name": "audio only",
            "categoryType": "media"
        }
    },
    {
        "id": 7,
        "isAuthoringComplete": true,
        "showOnLandingPage": true,
        "youtubeVideoId": "tNh2kjmSzPw",
        "soundCloudTrackId": "170049166",
        "thumbnailURL": "https://farm8.staticflickr.com/7496/15609516053_67a9b050e9_o.png",
        "duration": "05:45",
        "singers": [
            {
                "id": 7,
                "name": "Parvathy Baul",
                "hindiName": "",
                "primaryOccupation": ""
            },
            {
                "id": 26,
                "name": "Badar Muneer",
                "hindiName": "",
                "primaryOccupation": ""
            }
        ],
        "poets": [
            {
                "id": 8,
                "name": "Roshik",
                "hindiName": "",
                "primaryOccupation": ""
            }
        ],
        "downloadURL": "",
        "about": null,
        "songTitle": {
            "id": 39,
            "originalTitle": "किछु दिन मोने मोने",
            "englishTranslation": "For a Few Days, O Heart",
            "englishTransliteration": "Kichhu din mone mone",
            "category": {
                "id": 7,
                "name": "Song Title",
                "categoryType": "Song Title"
            }
        },
        "mediaCategory": null
    }
];
var song = {
    "id": 1,
    "englishTranslationTitle": "For a few days,O Heart",
    "englishTransliterationTitle": "Kichhu din mone mone",
    "singers": [],
    "poet": [
        {
            "id": 2,
            "name": "Roshik",
            "hindiName": "",
            "primaryOccupation": null
        }
    ],
    "duration": "5:45",
    "category": "Songs",
    "thumbnailUrl": "http://3.bp.blogspot.com/-kwpgiMcXc24/TcOcowo6mTI/AAAAAAAAA9w/uNt6ZsJadDg/s1600/parvathy_baul03.jpg",
    "publish": true,
    "contentFormat": "video"
};