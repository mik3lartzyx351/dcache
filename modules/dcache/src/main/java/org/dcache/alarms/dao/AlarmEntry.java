/*
COPYRIGHT STATUS:
Dec 1st 2001, Fermi National Accelerator Laboratory (FNAL) documents and
software are sponsored by the U.S. Department of Energy under Contract No.
DE-AC02-76CH03000. Therefore, the U.S. Government retains a  world-wide
non-exclusive, royalty-free license to publish or reproduce these documents
and software for U.S. Government purposes.  All documents and software
available from this server are protected under the U.S. and Foreign
Copyright Laws, and FNAL reserves all rights.

Distribution of the software available from this server is free of
charge subject to the user following the terms of the Fermitools
Software Legal Information.

Redistribution and/or modification of the software shall be accompanied
by the Fermitools Software Legal Information  (including the copyright
notice).

The user is asked to feed back problems, benefits, and/or suggestions
about the software to the Fermilab Software Providers.

Neither the name of Fermilab, the  URA, nor the names of the contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.

DISCLAIMER OF LIABILITY (BSD):

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED  WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED  WARRANTIES OF MERCHANTABILITY AND FITNESS
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL FERMILAB,
OR THE URA, OR THE U.S. DEPARTMENT of ENERGY, OR CONTRIBUTORS BE LIABLE
FOR  ANY  DIRECT, INDIRECT,  INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
OF SUBSTITUTE  GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY  OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT  OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE  POSSIBILITY OF SUCH DAMAGE.

Liabilities of the Government:

This software is provided by URA, independent from its Prime Contract
with the U.S. Department of Energy. URA is acting independently from
the Government and in its own private capacity and is not acting on
behalf of the U.S. Government, nor as its contractor nor its agent.
Correspondingly, it is understood and agreed that the U.S. Government
has no connection to this software and in no manner whatsoever shall
be liable for nor assume any responsibility or obligation for any claim,
cost, or damages arising out of or resulting from the use of the software
available from this server.

Export Control:

All documents and software available from this server are subject to U.S.
export control laws.  Anyone downloading information from this server is
obligated to secure any necessary Government licenses before exporting
documents or software obtained from this server.
 */
package org.dcache.alarms.dao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import org.dcache.alarms.IAlarms;
import org.dcache.alarms.Severity;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Preconditions;

/**
 * Storage class for all Alarm types.<br>
 * <br>
 *
 * Like {@link AbstractAlarm}, it uses the unique key for hashCode and equals.
 * Also implements comparable on the basis of the unique key.
 *
 * @author arossi
 */
public class AlarmEntry implements IAlarms, Comparable<AlarmEntry>,
                Serializable {

    private static final long serialVersionUID = -8477649701971508910L;
    private static final String FORMAT = "E MMM dd HH:mm:ss zzz yyyy";

    @Nonnull
    private String key;
    private Long timestamp;
    private String type;
    private Integer severity;
    private String host;
    private String domain;
    private String service;
    private String info;
    private String notes;
    private boolean closed = false;
    private int count = 1;

    /**
     * Needs to be here for database dehydration.
     */
    public AlarmEntry() {
    }

    /**
     * Extracts the basic properties from the JSON map.  Note that
     * all fields must be defined or the constructor will fail with
     * a JSONException.
     */
    public AlarmEntry(JSONObject json) throws JSONException {
        key = String.valueOf(json.get(KEY_TAG));
        type = String.valueOf(json.get(TYPE_TAG));
        host = String.valueOf(json.get(HOST_TAG));
        domain = String.valueOf(json.get(DOMAIN_TAG));
        service = String.valueOf(json.get(SERVICE_TAG));
        timestamp = (Long) json.get(TIMESTAMP_TAG);
        severity = Severity.valueOf((String) json.get(SEVERITY_TAG)).ordinal();
        info = String.valueOf(json.get(MESSAGE_TAG));
    }

    @Override
    public int compareTo(AlarmEntry o) {
        Preconditions.checkNotNull(o, "alarm entry parameter was null");
        return key.compareTo(o.key);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AlarmEntry)) {
            return false;
        }
        return key.equals(((AlarmEntry)other).key);
    }

    public int getCount() {
        return count;
    }

    public Date getDate() {
        return new Date(timestamp);
    }

    public String getDomain() {
        return domain;
    }

    public String getFormattedDate() {
        DateFormat format = new SimpleDateFormat(FORMAT);
        format.setLenient(false);
        return format.format(getDate());
    }

    public String getHost() {
        return host;
    }

    public String getInfo() {
        return info;
    }

    public String getKey() {
        return key;
    }

    public String getNotes() {
        return notes;
    }

    public String getService() {
        return service;
    }

    public Integer getSeverity() {
        return severity;
    }

    public Severity getSeverityEnum() {
        return Severity.fromOrdinal(severity);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
       return key.hashCode();
    }

    public void incrementCount() {
        ++count;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDate(Date date) {
        timestamp = date.getTime();
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setKey(String key) {
        Preconditions.checkNotNull(key, "key is null");
        this.key = key;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Constructed in order to be able to do expression matching.
     */
    @Override
    public String toString() {
        return new StringBuffer(getFormattedDate())
            .append(" ").append(type)
            .append(" ").append(getSeverityEnum())
            .append(" ").append(count)
            .append(" ").append(host)
            .append(" ").append(domain)
            .append(" ").append(info)
            .append(" ").append(service)
            .append(" ").append(notes).toString();
    }

    /**
     * Sets <code>closed</code> and <code>notes</code> fields.
     *
     * @param alarmEntry
     *            from which to get updatable values.
     */
    public void update(AlarmEntry alarmEntry) {
        if (alarmEntry == null) {
            return;
        }
        closed = alarmEntry.isClosed();
        notes = alarmEntry.getNotes();
    }
}
