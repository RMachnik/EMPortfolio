$('#sendEmailBtn').click(function () {
    var mailFrom = $('#name').val();
    var author = $('#email').val();
    var message = $('#message').val();
    $.ajax({
        type: 'POST',
        url: 'https://mandrillapp.com/api/1.0/messages/send.json',
        data: {
            'key': 'mZ41ty7LBB8rAaHvp9DAQg',
            'message': {
                'from_email': author,
                'to': [
                    {
                        'email': 'ewelina.salagaj@gmail.com',
                        'name': '',
                        'type': 'to'
                    }
                ],
                'autotext': 'true',
                'subject': author,
                'html': message
            }
        }
    }).done(function (response) {
        console.log(response);
        $('#name').val('');
        $('#email').val('');
        $('#message').val('');
        $('#successMsg').removeClass('hidden');
    }).error(function (error) {
        console.log(error);
    });
});