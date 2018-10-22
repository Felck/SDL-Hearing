pragma solidity ^0.4.21;
contract OrderDatabase {
    event NewOrderPlaced(
        address orderer,
        uint orderID
    );
    
    struct AcceptedOffer {
        address offerer;
        uint id;
    }
    struct Order {
        string data;
        bool closed;
        bool completed;
        AcceptedOffer[] accepted;
        // byte32[][] wäre schöner (weniger gas), aber Größe der Daten müsste bekannt sein
        mapping(address=>string[]) offers;
        // array for iteration through offers (mapping is not iterable)
        address[] offerers;
    }

    // für große Anzahl an Orders nested mapping evtl. sinnvoll
    mapping(address => Order[]) orders;

    function placeOrder(string data) public returns (uint id) {
        // create new order instance and get id
        uint orderID = orders[msg.sender].length++;
        // insert data
        orders[msg.sender][orderID].data = data;
        // emit event
        emit NewOrderPlaced(msg.sender, orderID);
        return orderID;
    }

    function placeOffer(address orderer, uint orderID, string data) public returns (uint id) {
        // require existence of specified order
        require(orders[orderer].length > orderID);
        // storage pointer to specified order
        Order storage o = orders[orderer][orderID];
        // require order not being closed
        require(o.closed == false);
        // if first offer from this address, save address for later iteration
        if(o.offers[msg.sender].length == 0) {
            o.offerers.push(msg.sender);
        }
        // push data
        o.offers[msg.sender].push(data);
        // return offerID
        return o.offers[msg.sender].length - 1;
    }
    
    function getOrder(address orderer, uint orderID) public view returns (string data) {
        // require existence of specified order
        require(orders[orderer].length > orderID);
        // return order data
        return orders[orderer][orderID].data;
    }
    
    function getOfferers(uint orderID) public view returns (address[] offerers) {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // return offerers
        return orders[msg.sender][orderID].offerers;
    }
    
    function getOfferCount(uint orderID, address offerer) public view returns (uint offerCount) {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // get offer count
        offerCount = orders[msg.sender][orderID].offers[offerer].length;
        // require existence of offers
        require(offerCount > 0);
        // return offer count
        return offerCount;
    }
    
    function getOffer(uint orderID, address offerer, uint offerID) public view returns (string data) {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // storage pointer to specified offers array
        string[] storage offers = orders[msg.sender][orderID].offers[offerer];
        // require existence of specified offer
        require(offers.length > offerID);
        // return data
        return offers[offerID];
    }
    
    function closeOrder(uint orderID) public {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // close order
        orders[msg.sender][orderID].closed = true;
    }
    
    function isClosed(address orderer, uint orderID) public view returns (bool closed) {
        // require existence of specified order
        require(orders[orderer].length > orderID);
        return orders[orderer][orderID].closed;
    }
    
    function acceptOffer(uint orderID, address offerer, uint offerID) public {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // storage pointer to specified order
        Order storage o = orders[msg.sender][orderID];
        // require order to be closed but not completed
        require(o.closed == true && o.completed == false);
        // require existence of specified offer
        require(o.offers[offerer].length > offerID);
        // add offer to accepted ones
        uint index = o.accepted.length++;
        o.accepted[index].offerer = offerer;
        o.accepted[index].id = offerID;
    }
    
    function completeOrder(uint orderID) public {
        // require existence of specified order
        require(orders[msg.sender].length > orderID);
        // mark order as completed
        orders[msg.sender][orderID].completed = true;
    }

    function isCompleted(address orderer, uint orderID) public view returns (bool completed) {
        // require existence of specified order
        require(orders[orderer].length > orderID);
        return orders[orderer][orderID].completed;
    }
    
    // for debugging only
    function close() public {
        selfdestruct(msg.sender);
    }

}
