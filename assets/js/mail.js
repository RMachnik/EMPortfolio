$('#sendEmailBtn').click(function () {
    $.ajax({
        type: 'POST',
        url: 'https://mandrillapp.com/api/1.0/messages/send.json',
        data: {
            'key': 'mZ41ty7LBB8rAaHvp9DAQg',
            'message': {
                'from_email': 'zenek@EMAIL.HERE',
                'to': [
                    {
                        'email': 'rafik991@gmail.com',
                        'name': 'RECIPIENT NAME (OPTIONAL)',
                        'type': 'to'
                    }
                ],
                'autotext': 'true',
                'subject': 'YOUR SUBJECT HERE!',
                'html': 'YOUR EMAIL CONTENT HERE! YOU CAN USE HTML!'
            }
        }
    }).done(function(response) {
        console.log(response); // if you're into that sorta thing
    });
});