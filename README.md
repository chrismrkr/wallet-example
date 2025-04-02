# wallet-example

## Quick Start

### Create Image
+ git clone https://github.com/chrismrkr/wallet-example.git
+ Build Jar File : ./gradlew clean build -x test
+ Build Image with Dockerfile : ```docker build -t wallet-example ./```

### Run in Docker Compose
+ ```docker compose up -d```

### API Test

#### Create Balance
```dtd
POST http://localhost/api/balance/create
{
    "balanceId": 1,
    "memberId": 1,
    "amount": "1000000"
}
```

```dtd
POST http://localhost/api/balance/create
{
    "balanceId": 2,
    "memberId": 2,
    "amount": "1000000"
}
```

#### Transfer Balance 1 to 2
```dtd
POST http://localhost/api/balance/transfer
{
    "eventId": 5,
    "senderBalanceId": "1",
    "receiverBalanceId": "2",
    "amount": "1000",
    "senderId": 1,
    "receiverId": 2
}
```