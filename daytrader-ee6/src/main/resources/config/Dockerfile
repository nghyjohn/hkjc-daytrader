FROM websphere-liberty:kernel
COPY wlp/usr/servers/Daytrader3Sample /opt/ibm/wlp/usr/servers/defaultServer
RUN installUtility install --acceptLicense defaultServer
RUN rm -rf /opt/ibm/wlp/usr/servers/defaultServer/Daytrader3Sample
