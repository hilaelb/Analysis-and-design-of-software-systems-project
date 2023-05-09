package businessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.DistanceBetweenSites;
import dataAccessLayer.transportModule.SitesDAO;
import dataAccessLayer.transportModule.SitesDistancesDAO;
import javafx.util.Pair;
import objects.transportObjects.Site;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.transportUtils.TransportException;

import java.util.*;

import static serviceLayer.employeeModule.Services.UserService.TRANSPORT_MANAGER_USERNAME;

/**
 * The `SitesController` class is responsible for managing the sites in the TransportModule.
 * It provides methods for adding, removing, updating, and retrieving sites.
 */
public class SitesController {
    private final SitesDAO dao;
    private final SitesDistancesDAO distancesDAO;
    private EmployeesService employeesService;
    private SitesDistancesController distancesController;
    
    public SitesController(SitesDAO dao, SitesDistancesDAO distancesDAO){
        this.dao = dao;
        this.distancesDAO = distancesDAO;
    }

    public void injectDependencies(EmployeesService employeesService) {
        this.employeesService = employeesService;
    }

    /**
     * Adds a site to the `SitesController`.
     *
     * @param site The site to be added.
     * @throws TransportException If the site already exists.
     */
    public void addSite(Site site) throws TransportException {
        if (siteExists(site.address()) != false) {
            throw new TransportException("Site already exists");
        }
        try {
            dao.insert(site);
            distancesDAO.insertAll(distancesController.createDistanceObjects(site, getAllSites()));
            if(site.siteType() == Site.SiteType.BRANCH){
                employeesService.createBranch(TRANSPORT_MANAGER_USERNAME,site.address());
            }
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a site from the `SitesController`.
     *
     * @param address The address of the site to be removed.
     * @throws TransportException If the site is not found.
     */
    public void removeSite(String address) throws TransportException {
        if (siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        try {
            dao.delete(Site.getLookupObject(address));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves a site from the `SitesController`.
     *
     * @param address The address of the site to be retrieved.
     * @return The retrieved site.
     * @throws TransportException If the site is not found.
     */
    public Site getSite(String address) throws TransportException {
        if (siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        try {
            return dao.select(Site.getLookupObject(address));
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Updates a site in the `SitesController`.
     *
     * @param address The address of the site to be updated.
     * @param newSite The updated site.
     * @throws TransportException If the site is not found.
     */
    public void updateSite(String address, Site newSite) throws TransportException{
        if(siteExists(address) == false) {
            throw new TransportException("Site not found");
        }

        try {
            dao.update(newSite);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    /**
     * Retrieves all sites from the `SitesController`.
     *
     * @return A linked list of all sites.
     */
    public List<Site> getAllSites() throws TransportException {
        try {
            return dao.selectAll();
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public boolean siteExists(String address) throws TransportException{
        Site lookupObject = Site.getLookupObject(address);
        try {
            return dao.exists(lookupObject);
        } catch (DalException e) {
            throw new TransportException(e.getMessage(),e);
        }
    }

    public Map<Pair<String,String>,Double> buildSitesDistances(List<String> route) throws TransportException {
        HashMap<Pair<String,String>,Double> distances = new HashMap<>();
        ListIterator<String> destinationsIterator = route.listIterator();
        String curr = destinationsIterator.next();
        String next;

        // map distances between following sites
        while (destinationsIterator.hasNext()) {
            next = destinationsIterator.next();
            DistanceBetweenSites lookUpObject = DistanceBetweenSites.getLookupObject(curr,next);
            double distance;
            try {
                distance = distancesDAO.select(lookUpObject).distance();
            } catch (DalException e) {
                throw new TransportException(e.getMessage(),e);
            }
            distances.put(new Pair<>(curr,next),distance);
            curr = next;
        }
        return distances;
    }


}
