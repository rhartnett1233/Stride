

from __future__ import print_function # Python 2/3 compatibility
import boto3
import json
import decimal
from boto3.dynamodb.conditions import Key, Attr

# Helper class to convert a DynamoDB item to JSON.
class DecimalEncoder(json.JSONEncoder):
    def default(self, o):
        if isinstance(o, decimal.Decimal):
            if o % 1 > 0:
                return float(o)
            else:
                return int(o)
        return super(DecimalEncoder, self).default(o)

dynamodb = boto3.resource('dynamodb', region_name='us-west-2', endpoint_url="http://localhost:7980")

table = dynamodb.Table('Movies')

fe = Key('year').between(1950, 1959)
pe = "#yr, title, info.rating"
# Expression Attribute Names for Projection Expression only.
ean = { "#yr": "year", }
esk = None

###################################################################################
# -- ProjectionExpression specifies the attributes you want in the scan result.
#
# -- FilterExpression specifies a condition that returns only items that satisfy 
#		the condition. All other items are discarded.
#
# -- The scan method returns a subset of the items each time, called a page. The 
#		LastEvaluatedKey value in the response is then passed to the scan method 
#		via the ExclusiveStartKey parameter. When the last page is returned, 
#		LastEvaluatedKey is not part of the response.
#
# -- ExpressionAttributeNames provides name substitution. We use this because
#		 year is a reserved word in DynamoDB—you can't use it directly in any 
#		expression, including KeyConditionExpression. You can use the expression 
#		attribute name #yr to address this.
#
# -- ExpressionAttributeValues provides value substitution. You use this because 
#		you can't use literals in any expression, including KeyConditionExpression. 
#		You can use the expression attribute value :yyyy to address this.
###################################################################################
response = table.scan(
    FilterExpression=fe,
    ProjectionExpression=pe,
    ExpressionAttributeNames=ean
    )

for i in response['Items']:
    print(json.dumps(i, cls=DecimalEncoder))

while 'LastEvaluatedKey' in response:
    response = table.scan(
        ProjectionExpression=pe,
        FilterExpression=fe,
        ExpressionAttributeNames= ean,
        ExclusiveStartKey=response['LastEvaluatedKey']
        )

    for i in response['Items']:
        print(json.dumps(i, cls=DecimalEncoder))