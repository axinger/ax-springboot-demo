$(data).each(function (i, item) {
    innerHTML += '<li type-id="' + item.id + '">' +
        '<video style="width:100%;height:100%;" id="example_video_' + item.id + '"class="videoClass video-js vjs-default-skin vjs-big-play-centered" autoplay="autoplay" controls muted><source src="' + item.hls + '" type="application/x-mpegURL">' +
        '</video>' +
        '<p>' + item.name + '</p>' +
        '</li>';
    //视频实时播放
    setTimeout(function () {
        var myPlayer = videojs('example_video_' + item.id);
        videojs('example_video_' + item.id).ready(function () {
            var myPlayer = this;
            myPlayer.play();
        });
    }, 5000);
});
setTimeout(function () {
    var myPlayer = videojs('example_video_' + item.id);
    videojs('example_video_' + item.id).ready(function () {
        var myPlayer = this;
        myPlayer.play();
    });
}, 5000);
