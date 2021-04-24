---KEYS[1] == user profile id key profile:<user id>
---KEYS[2] == table occupancy key table:occupancy:<table id>
---KEYS[3] == user reservation key reservation:users
---KEYS[4] == table reservation key reservation:table:<table id>
---KEYS[5] == table logs key log:tables:<table id>
---KEYS[6] == user logs key log:users:<user id>
---KEYS[7] == queue list key name -  queue
---ARGV[1] User id
---ARGV[2] data JSON

--- validate user profile
if redis.call("EXISTS", KEYS[1]) == 1 then
    --- validate if User has a reservation
    if redis.call("HEXISTS", KEYS[3], ARGV[1]) == 1 then
        --- Decrease Occupancy
        redis.call("DECR", KEYS[2])
        ---- Remove reservation on table
        redis.call("HDEL", KEYS[4], ARGV[1])
        ---- Remove reservation on user
        redis.call("HDEL", KEYS[3], ARGV[1])
        ---- Add Logs to table
        redis.call("LPUSH", KEYS[5], ARGV[2])
        ---- Add Logs to user
        redis.call("LPUSH", KEYS[6], ARGV[2])
        ---- publish event
        redis.call("RPUSH", KEYS[7], ARGV[2])
        return "OK"
    else
        return "User does not have a reservation"
    end
else
    return "Profile does not exist"
end