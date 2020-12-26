all = {
    'shop': {
        'no-login': [
            r'/shops/states',
        ],
        'user': [],
        'admin': [
            r'/shops', r'/shops/{id}', r'/shops/{shopId}/newshops/{id}/audit',
            r'/shops/{id}/onshelves', r'/shops/{id}/offshelves'
        ]
    },
    'groupon': {
        'no-login': [
            r'/groupons/states',
        ],
        'user': [
            r'/groupons',
        ],
        'admin': [
            r'/shops/{id}/groupons', r'/shops/{shopId}/spus/{id}/groupons',
            r'/shops/{shopId}/groupons/{id}',
            r'/shops/{shopId}/groupons/{id}/onshelves',
            r'/shops/{shopId}/groupons/{id}/offshelves'
        ]
    },
    'presales': {
        'no-login': [
            r'/presales/states',
            r'/presales',
        ],
        'user': [],
        'admin': [
            r'/shops/{shopId}/presales',
            r'/shops/{shopId}/skus/{id}/presales',
            r'/shops/{shopId}/presales/{id}',
            r'/shops/{shopId}/presales/{id}/onshelves',
            r'/shops/{shopId}/presales/{id}/offshelves',
        ]
    },
    'flash': {
        'no-login': [
            r'/timesegments/{id}/flashsales',
            r'/flashsales/current',
        ],
        'user': [],
        'admin': [
            r'/shops/{did}/timesegments/{id}/flashsales',
            r'​/shops​/{did}​/flashsales​/{id}',
            r'/shops/{did}/flashsales/{id}/onshelves',
            r'/shops/{did}/flashsales/{id}/offshelves',
            r'/shops/{did}/flashsales/{id}/flashitems',
            r'/shops/{did}/flashsales/{fid}/flashitems/{id}'
        ]
    },
    'goods': {
        'no-login': [r'/skus/states', r'/skus', r'/skus/{id}', r'/spus/{id}'],
        'user': [
            r'/share/{sid}/skus/{id}',
        ],
        'admin': [
            r'/shops/{shopId}/spus/{id}/skus',
            r'/shops/{shopId}/skus/{id}/uploadImg',
            r'/shops/{shopId}/skus/{id}',
            r'/shops/{id}/spus',
            r'/shops/{shopId}/spus/{id}/uploadImg',
            r'/shops/{shopId}/spus/{id}',
            r'/shops/{shopId}/skus/{id}/onshelves',
            r'/shops/{shopId}/skus/{id}/offshelves',
            r'/shops/{shopId}/skus/{id}/floatPrices',
            r'/shops/{shopId}/floatPrices/{id}',
        ]
    },
    'comments': {
        'no-login': [
            r'/comments/states',
            r'/skus/{id}/comments',
        ],
        'user': [r'/orderitems/{id}/comments', r'/comments'],
        'admin':
        [r'/shops/{did}/comments/{id}/confirm', r'/shops/{id}/comments/all']
    },
    'category': {
        'no-login': [r'/categories/{id}/subcategories', r'/brands'],
        'user': [],
        'admin': [
            r'/shops/{shopId}/categories/{id}/subcategories',
            r'/shops/{shopId}/categories/{id}',
            r'/shops/{shopId}/categories/{id}',
            r'/shops/{id}/brands',
            r'/shops/{shopId}/brands/{id}/uploadImg',
            r'/shops/{shopId}/brands/{id}',
            r'/shops/{shopId}/brands/{id}',
            r'/shops/{shopId}/spus/{spuId}/categories/{id}',
            r'/shops/{shopId}/spus/{spuId}/categories/{id}',
            r'/shops/{shopId}/spus/{spuId}/brands/{id}',
            r'/shops/{shopId}/spus/{spuId}/brands/{id}',
        ]
    },
    'coupon': {
        'no-login': [
            r'/coupons/states',
            r'/couponactivities',
            r'/couponactivities/{id}/skus',
        ],
        'user': [
            r'/shops/{shopId}/couponactivities/{id}',
            r'/coupons',
            r'/couponactivities/{id}/usercoupons',
        ],
        'admin': [
            r'/shops/{shopId}/couponactivities',
            r'/shops/{shopId}/couponactivities/{id}/uploadImg',
            r'/shops/{id}/couponactivities/invalid',
            r'/shops/{shopId}/couponactivities/{id}',
            r'/shops/{shopId}/couponactivities/{id}/skus',
            r'/shops/{shopId}/couponskus/{id}',
            r'/shops/{shopId}/couponactivities/{id}/onshelves',
            r'/shops/{shopId}/couponactivities/{id}/offshelves',
        ]
    }
}

shop_uri = 'http://172.16.1.181:8080/'

for k, v in all.items():
    for t, s in v.items():
        if len(s) > 0:
            id = k + '-' + t
            path = ','.join(s)
            print(f'- id: {id}')
            print(f'  uri: {shop_uri}')
            print(f'  predicates:')
            print(f'  - Path={path}')
            if 'admin' in t or 'user' in t:
                print(f'  filters:')
                print(f'  - Auth=authorization')
