path = [
  # comment
  "/comments",
  "/comments/states",
  "/orderItems/*/comments",
  "/shops/*/comments/**",
  "/skus/*/comments",
  # coupon
  "/coupons/**",
  "/couponactivities/**",
  "/shops/*/couponactivities/**",
  "/shops/*/couponskus/**",
  # flashsale
  "/flashsales/current",
  "/shops/*/timesegments/*/flashsales",
  "/timesegments/*/flashsales",
  "/shops/*/flashsales/**",
  # groupon
  "/groupons/**",
  "/shops/*/groupons/**",
  "/shops/*/spus/*/groupons",
  # presale
  "/presales/**",
  "/shops/*/presales/**",
  "/shops/*/skus/*/presales",
  # shop
  "/shops/*",
  "/shops/*/newshops/**",
  "/shops/*/onshelves",
  "/shops/*/offshelves",
  # brands
  "/brands",
  "/shops/*/brands",
  "/shops/*/brands/**",
  "/shops/*/spus/*/brands/**",
  "/categories/**",
  "/shops/*/categories/**",
  "/shops/*/spus/*/categories/**",
  # share
  "/share/*/skus/*",
  # float price
  "/shops/*/skus/*/floatPrices",
  "/shops/*/floatPrices/*",
  # sku
  "/skus/states",
  "/shops/*/skus/*/",
  "/shops/*/skus/*/uploadImg",
  "/shops/*/skus/*/onshelves",
  "/shops/*/skus/*/offshelves",
  # spu
  "/spus/*/",
  "/shops/*/spus/**",
]

print(','.join(path))