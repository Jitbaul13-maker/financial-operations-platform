local current = redis.call('GET', KEYS[1])

if not current then
    return 0
end

local record = cjson.decode(current)

if record.status ~= 'PROCESSING' then
    return 1
end

local newRecord = cjson.encode({
    status = 'COMPLETED',
    requestFingerprint = record.requestFingerprint,
    txnId = ARGV[1]
})

redis.call('SET', KEYS[1], newRecord, 'EX', 86400)

return 2