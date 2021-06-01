#!/bin/bash

CLIENT_ID=$1
SECRET_HASH=$2
USERNAME=$3
PASSWORD=$4

aws cognito-idp initiate-auth \
--auth-flow USER_PASSWORD_AUTH \
--auth-parameters "SECRET_HASH=$2,USERNAME=$3,PASSWORD=$4" \
--query "AuthenticationResult.IdToken" \
--client-id $1
