let awardId = 1;
let expert_id = 0;
let fetchUnvoteInterval;
let fetchNoPreVotedInterval;

function getQueryVariable(variable) {
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i=0;i<vars.length;i++) {
        let pair = vars[i].split("=");
        if(pair[0] === variable){return pair[1];}
    }
    return "";
}


$(function () {

    awardId = getQueryVariable("id")

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-top-center",
        "preventDuplicates": true,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "2000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    $.ajax({
        url: "/admin/result/view/" + awardId,
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: (result) => {
            console.log(result);
            if (result['code'] === 'FAILURE') {
                toastr.options.timeout = 2000;
                toastr.error(result['reason']);
            } else {
                let awardType = result['content']['award']['type'];
                let entries = result['content']['vote'];
                console.log(entries);
                const voteHead = $('#voteHeader');
                const voteBody = $('#voteBody');
                if (awardType === 1) {
                    voteHead.append(
                        '<th class="text-center">序号</th>' +
                        '<th class="text-center">奖项</th> ' +
                        '<th class="text-center">同意授奖</th>' +
                        '<th class="text-center">不同意授奖</th>' +
                        '<th class="text-center">获奖结果</th>'
                    );
                    for(let i=0,len=entries.length;i<len;++i){
                        voteBody.append(`<tr>
                        <td id="entryId">${entries[i]['id']}</td>
                        <td id="entry_name">${entries[i]['entry_name']}</td>
                        <td id="first_level">${entries[i]['level1']}</td>
                        <td id="third_level">${entries[i]['level3']}</td>
                        <td id="second_level">${entries[i]['entry_prize']}</td>
                    </tr> `);
                    }
                } else {
                    voteHead.append(
                        '<th class="text-center">序号</th>' +
                        '<th class="text-center">奖项</th> ' +
                        '<th class="text-center">一等奖</th>' +
                        '<th class="text-center">二等奖</th>' +
                        '<th class="text-center">三等奖</th>' +
                        '<th class="text-center">获奖结果</th>'
                    );
                    for(let i=0,len=entries.length;i<len;++i){
                        voteBody.append(`<tr>
                        <td id="entryId">${entries[i]['id']}</td>
                        <td id="entry_name">${entries[i]['entry_name']}</td>
                        <td id="first_level">${entries[i]['level1']}</td>
                        <td id="first_level">${entries[i]['level2']}</td>
                        <td id="third_level">${entries[i]['level3']}</td>
                        <td id="second_level">${entries[i]['entry_prize']}</td>
                    </tr> `);
                    }

                }
            }
        }
    });
});