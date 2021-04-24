---KEYS[1] == user profile id key profile:<user id>
---KEYS[2] == table occupancy key table:occupancy:<table id>
---KEYS[3] == user reservation key reservation:users
---KEYS[4] == table reservation key reservation:table:<table id>
---KEYS[5] == table logs key log:tables:<table id>
---KEYS[6] == user logs key log:users:<user id>
---KEYS[7] == queue list key name -  queue
---ARGV[1] max chairs (integer)
---ARGV[2] User id
---ARGV[3] data JSON

--- validate user profile
if redis.call("EXISTS", KEYS[1]) == 1 then
    --- validate table occupancy
    if tonumber(redis.call("GET", KEYS[2])) < tonumber(ARGV[1]) then
        --- validate User does not have other reservations
        if redis.call("HEXISTS", KEYS[3], ARGV[2]) == 0 then
            --- Increase Occupancy
            redis.call("INCR", KEYS[2])
            ---- Save reservation on table
            redis.call("HSET", KEYS[4], ARGV[2], ARGV[3])
            ---- Save reservation on user
            redis.call("HSET", KEYS[3], ARGV[2], ARGV[3])
            ---- Add Logs to table
            redis.call("LPUSH", KEYS[5], ARGV[3])
            ---- Add Logs to user
            redis.call("LPUSH", KEYS[6], ARGV[3])
            ---- publish event
            redis.call("RPUSH", KEYS[7], ARGV[3])
            return "OK"
        else
            return "User already have a reservation"

        end
    else
        return "Full table"
    end
else
    return "Profile does not exist"
end