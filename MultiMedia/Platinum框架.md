1. UPnP
    1. start
        1. socket: new NPT_UdpMulticastSocke(NPT_SOCKET_FLAG_CANCELLABLE)
            1. delegate: NPT_BsdUdpMulticastSocket
        2. socket->Bind(NPT_SocketAddress(NPT_IpAddress::Any, 1900), true)