$('#sendEmailBtn').click(function () {
    var mailFrom = $('#name').val();
    var author = $('#email').val();
    var message = $('#message').val();
    var ewelinaEmail = 'ewelina.salagaj@gmail.com'
    $.ajax({
        type: 'POST',
        url: 'https://mandrillapp.com/api/1.0/messages/send.json',
        data: {
            'key': 'mZ41ty7LBB8rAaHvp9DAQg',
            'message': {
                'from_email': mailFrom,
                'to': [
                    {
                        'email': ewelinaEmail,
                        'name': '',
                        'type': 'to'
                    }
                ],
                'autotext': 'true',
                'subject': author,
                'html': message
            }
        }
    }).done(function(response) {
        console.log(response);
    })
        .always(function(data){
            console.log(mailFrom);
            console.log(author);
            console.log(message);
        });
});